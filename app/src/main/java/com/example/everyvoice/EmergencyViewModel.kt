//package com.example.everyvoice
//
//import android.telephony.SmsManager
//import androidx.lifecycle.ViewModel
//
//class EmergencyViewModel: ViewModel() {
//
//
//    fun sendSmsDirect(phoneNumber: String, message: String) {
//        try {
//            // Handle different Android versions
//            val smsManager: SmsManager =
//                SmsManager.getDefault()
//
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
//            println("SMS Sent Successfully")
//        } catch (e: Exception) {
//            println("Error sending SMS: ${e.localizedMessage}")
//        }
//    }
//}