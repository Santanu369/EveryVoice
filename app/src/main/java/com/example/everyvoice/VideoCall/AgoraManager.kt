package com.example.everyvoice.VideoCall

// AgoraManager.kt
import android.content.Context
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas

class AgoraManager(val context: Context, val appId: String) {
    private var rtcEngine: RtcEngine? = null
    var eventHandler: IRtcEngineEventHandler? = null

    fun setupRtcEngine() {
        try {
            rtcEngine = RtcEngine.create(context, appId, eventHandler)
            rtcEngine?.addHandler(eventHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun joinChannel(channelName: String, uid: Int) {
        rtcEngine?.enableVideo()
        rtcEngine?.joinChannel(null, channelName, "", uid)
    }

    fun leaveChannel() {
        rtcEngine?.leaveChannel()
    }

    fun setupLocalVideo(videoView: android.view.SurfaceView) {
        val canvas = VideoCanvas(videoView)
        rtcEngine?.setupLocalVideo(canvas)
    }

    fun setupRemoteVideo(videoView: android.view.SurfaceView, uid: Int) {
        val canvas = VideoCanvas(videoView)
        canvas.uid = uid
        rtcEngine?.setupRemoteVideo(canvas)
    }

    fun muteAudio(mute: Boolean) {
        rtcEngine?.muteLocalAudioStream(mute)
    }

    fun muteVideo(mute: Boolean) {
        rtcEngine?.muteLocalVideoStream(mute)
    }

    fun release() {
        rtcEngine?.leaveChannel()
        RtcEngine.destroy()
        rtcEngine = null
    }
}