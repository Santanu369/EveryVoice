//package com.example.everyvoice
//
//import com.google.android.gms.location.LocationServices
//
//val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//@SuppressLint("MissingPermission")
//fun getLocation(onResult: (String) -> Unit) {
//    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//        if (location != null) {
//            val lat = location.latitude
//            val lon = location.longitude
//
//            val mapLink = "https://maps.google.com/?q=$lat,$lon"
//            onResult(mapLink)
//        }
//    }
//}