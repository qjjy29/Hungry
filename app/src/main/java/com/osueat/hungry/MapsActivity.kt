package com.osueat.hungry

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

import com.osueat.hungry.services.gms.DirectionPointListener

import com.osueat.hungry.services.gms.GetPathFromLocation
import com.osueat.hungry.services.gms.UserLocation


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    OnMyLocationButtonClickListener, OnMyLocationClickListener {

    private lateinit var mMap: GoogleMap
    private val api_key = "AIzaSyCS02VK-i6AMuzSNh09BFrseMcCAn-s_U8"
    private var targetLat = 0.0
    private var targetLng = 0.0
    private var targetName = "target"
    private val serverApiKey = "AIzaSyDhRFzvNS_W44tLXmTTk7-CmRKGZTZiLgw"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b: Bundle? = intent.extras

        if (b != null) {
            targetLat = b.getDouble("lat")
            targetLng = b.getDouble("lng")
            targetName = b.getString("name")!!
        }

        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val targetTruck = LatLng(targetLat, targetLng)
        mMap.addMarker(MarkerOptions().position(targetTruck).title(targetName))

        //Execute Directions API request
        UserLocation.updateLocation(this)
        val userLocation = UserLocation.getLocation()

        GetPathFromLocation(serverApiKey, userLocation, targetTruck, object : DirectionPointListener {
            override fun onPath(polyLine: PolylineOptions) {
                mMap.addPolyline(polyLine)
            }
        }).execute()

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(targetTruck))
        val truckLocation = CameraUpdateFactory.newLatLngZoom(targetTruck, 15.0f)
        mMap.animateCamera(truckLocation)

        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show();
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }
}
