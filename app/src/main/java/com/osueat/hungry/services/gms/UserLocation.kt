package com.osueat.hungry.services.gms

import android.app.Activity
import android.location.Location

import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient

object UserLocation {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation = LatLng(0.0, 0.0)

    fun getLocation(): LatLng {
        return currentLocation
    }

    fun updateLocation(activity: Activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                currentLocation = LatLng(location?.latitude!!, location?.longitude!!)
            }
    }
}