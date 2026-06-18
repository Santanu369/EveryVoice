package com.example.everyvoice.Authentication


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.everyvoice.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/* ----------------------------------------------------------------------
   COLOR TOKENS
   Chosen and checked for contrast, not just looks:
   - AccentBlue on white  ≈ 7.0:1   (passes AAA)
   - ErrorRed on white    ≈ 7.3:1   (passes AAA)
   - TextSecondary on white ≈ 5.1:1 (passes AA for normal text)
   ---------------------------------------------------------------------- */
private val Background = Color(0xFFFFFFFF)
private val FieldFill = Color(0xFFF2F2F7)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6E6E73)
private val AccentBlue = Color(0xFF0050C8)
private val ErrorRed = Color(0xFFB00020)
private val DividerColor = Color(0xFFD1D1D6)

/* ----------------------------------------------------------------------
   ENTRY POINT
   ---------------------------------------------------------------------- */
@Composable
fun SignUpScreen(
    navController: NavController,
    onGoogleClick: () -> Unit = {},
    onFacebookClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    fun onLoginClick() {
        navController.navigate("LogInScreen")
    }

    fun validateAndSubmit() {
        emailError = if (email.isBlank() || !email.trim().endsWith("@gmail.com")) "Enter a valid email address" else null
        passwordError = if (password.length < 6) "Password must be at least 6 characters" else null
        if (emailError == null && passwordError == null) {
            // create new user
            Firebase.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate("home")
                    }
                    else {
                        val exception = task.exception
                        val errorMessage = exception?.message ?: "An error occurred"
                        emailError = errorMessage
                    }
                }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(48.dp))

            // Marked as a heading so TalkBack users can jump screen structure
            // by heading, the same way sighted users scan with their eyes.
            Text(
                text = "Create your account",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.semantics { heading() }
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign up with your email, or continue with Google or Facebook.",
                color = TextSecondary,
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(36.dp))

            // Email — label is passed to the field itself, so TalkBack always
            // announces "Email, text field" correctly regardless of focus state.
            TextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                colors = accessibleFieldColors()
            )
            emailError?.let {
                // liveRegion = Polite makes TalkBack announce this automatically
                // the instant it appears, with no need to find and focus it.
                ErrorText(it)
            }

            Spacer(Modifier.height(20.dp))

            // Password — "Show/Hide" is a real TextButton, which guarantees a
            // 48dp+ touch target and a correctly announced button role.
            TextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Password") },
                singleLine = true,
                isError = passwordError != null,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            color = AccentBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    validateAndSubmit()
                }),
                colors = accessibleFieldColors()
            )
            passwordError?.let { ErrorText(it) }

            Spacer(Modifier.height(32.dp))

            // Primary action. heightIn (not a fixed height) lets the button grow
            // instead of clipping text when the user has system font scaling on.
            Button(
                onClick = { validateAndSubmit() },
                modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = Color.White)
            ) {
                Text("Create Account", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(28.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
                Text(
                    text = "or continue with",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
            }

            Spacer(Modifier.height(24.dp))

            SocialButton(
                label = "Continue with Google",
                badgeImage = R.drawable.google,
                badgeColor = Color(0xFF4285F4),
                onClick = onGoogleClick
            )
            Spacer(Modifier.height(14.dp))
            SocialButton(
                label = "Continue with Facebook",
                badgeImage = R.drawable.facebook,
                badgeColor = Color(0xFF1877F2),
                onClick = onFacebookClick
            )

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account?", color = TextSecondary, fontSize = 15.sp)
                TextButton(onClick = { onLoginClick() }) {
                    Text("Log in", color = AccentBlue, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

/* ----------------------------------------------------------------------
   REUSABLE PIECES
   ---------------------------------------------------------------------- */
@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        color = ErrorRed,
        fontSize = 14.sp,
        modifier = Modifier
            .padding(top = 6.dp, start = 4.dp)
            .semantics { liveRegion = LiveRegionMode.Polite }
    )
}

@Composable
private fun accessibleFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = FieldFill,
    unfocusedContainerColor = FieldFill,
    disabledContainerColor = FieldFill,
    errorContainerColor = FieldFill,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedLabelColor = AccentBlue,
    unfocusedLabelColor = TextSecondary,
    errorLabelColor = ErrorRed,
    cursorColor = AccentBlue,
    errorCursorColor = ErrorRed,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary
)

@Composable
private fun SocialButton(
    label: String,
    badgeImage: Int,
    badgeColor: Color,
    onClick: () -> Unit
) {
    // Using a real OutlinedButton (not a hand-rolled clickable Box) means we get
    // a correct Button role, ripple feedback, and a guaranteed 48dp+ touch target
    // for free, instead of having to reimplement all of that by hand.
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().heightIn(min = 56.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, DividerColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
                // Purely decorative letter badge — silenced so TalkBack doesn't
                // announce a stray "G" before the real label.
                .clearAndSetSemantics {},
            contentAlignment = Alignment.Center
        ) {
//            Text(text = badgeText, color = badgeColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Image(painter = painterResource(badgeImage), contentDescription = "google", modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

/* ----------------------------------------------------------------------
   PREVIEW
   ---------------------------------------------------------------------- */
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccessibleSignUpScreenPreview() {
    MaterialTheme {
//        SignUpScreen()
    }
}
