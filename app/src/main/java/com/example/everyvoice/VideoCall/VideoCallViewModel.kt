package com.example.everyvoice.VideoCall

// VideoCallViewModel.kt
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CallState(
    val isCallActive: Boolean = false,
    val remoteUserName: String = "",
    val localUserName: String = "",
    val isMuted: Boolean = false,
    val isCameraOff: Boolean = false
)

class VideoCallViewModel : ViewModel() {
    private val _callState = MutableStateFlow(CallState())
    val callState: StateFlow<CallState> = _callState

    fun startCall(remoteUserName: String, localUserName: String) {
        _callState.value = _callState.value.copy(
            isCallActive = true,
            remoteUserName = remoteUserName,
            localUserName = localUserName
        )
    }

    fun endCall() {
        _callState.value = _callState.value.copy(isCallActive = false)
    }

    fun toggleMute() {
        _callState.value = _callState.value.copy(
            isMuted = !_callState.value.isMuted
        )
    }

    fun toggleCamera() {
        _callState.value = _callState.value.copy(
            isCameraOff = !_callState.value.isCameraOff
        )
    }
}