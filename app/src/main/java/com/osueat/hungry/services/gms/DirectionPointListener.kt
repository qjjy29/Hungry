package com.osueat.hungry.services.gms

import com.google.android.gms.maps.model.PolylineOptions

interface DirectionPointListener {
    fun onPath(polyLine: PolylineOptions)
}