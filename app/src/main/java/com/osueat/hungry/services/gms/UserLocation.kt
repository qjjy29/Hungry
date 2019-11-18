package com.osueat.hungry.services.gms

import android.app.Activity
import android.location.Location
import android.util.Log

import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient

object UserLocation {
    private val TAG = "UserLocation"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation = LatLng(0.0, 0.0)

    fun getLocation(): LatLng {
        Log.i(TAG, "got user location ${currentLocation.latitude} ${currentLocation.longitude}")
        return currentLocation
    }

    fun updateLocation(activity: Activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                currentLocation = if (location == null) {
                    LatLng(0.0, 0.0)
                } else {
                    LatLng(location?.latitude!!, location?.longitude!!)
                }
                Log.d(TAG, "user location updated! ${currentLocation.latitude} ${currentLocation.longitude}")
            }
    }
}