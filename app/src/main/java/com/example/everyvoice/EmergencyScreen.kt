//package com.example.everyvoice
//
//import android.Manifest
//import android.telephony.SmsManager
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//
//@Composable
//fun EmergencyScreen() {
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val smsGranted = permissions[Manifest.permission.SEND_SMS] == true
//        val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
//
//        if (smsGranted && locationGranted) {
//            // safe to send SOS
//            getLocation {link->
//                val phoneNumber = "8250038439"
//                sendSms("+91$phoneNumber", "SOS help needed:\n$link")
//            }
//        }
//    }
//
//    Column(modifier = Modifier.fillMaxSize()
//        .clickable {
//            permissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.SEND_SMS,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            )
//        },
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally) {
//
//        Icon(painterResource(R.drawable.outline_sos_24), contentDescription = "SOS Icon")
//    }
//}
//
//fun sendSms(phone: String, message: String) {
//    val smsManager = SmsManager.getDefault()
//    smsManager.sendTextMessage(phone, null, message, null, null)
//}