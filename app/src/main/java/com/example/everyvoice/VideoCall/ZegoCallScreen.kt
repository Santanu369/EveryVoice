package com.example.everyvoice.VideoCall

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import kotlin.text.replace
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toLong
import androidx.compose.ui.platform.LocalContext
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment

@Composable
fun ZegoCallScreen(
    userID: String,
    userName: String,
    callID: String = "1234",
    appID: Long = 1981408850,
    appSign: String = "9d895850fbd653b7a01e3e52a1a6bc5fe487ac336f43ddbdbb07be82cdb829e6"
) {
    val context = LocalContext.current
    val activity = context as FragmentActivity

    val containerId = remember { View.generateViewId() }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            FragmentContainerView(it).apply {
                id = containerId
            }
        }
    )

    LaunchedEffect(Unit) {
        val fragment = ZegoUIKitPrebuiltCallFragment.newInstance(
            appID,
            appSign,
            userID,
            userName,
            callID,
            ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
        )

        activity.supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }
}