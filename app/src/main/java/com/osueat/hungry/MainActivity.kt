package com.osueat.hungry

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.FirebaseDatabase
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_FINE_LOCATION
import com.osueat.hungry.services.gms.UserLocation
import com.osueat.hungry.ui.customer.CustomerNearbyTruckActivity
import com.osueat.hungry.ui.customer.CustomerOrderHistoryActivity
import com.osueat.hungry.ui.customer.CustomerProfileActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance().reference

    private val TAG = "MainActivity"

    private val INITIAL_PERMS =
        arrayOf(ACCESS_FINE_LOCATION)

    private val INITIAL_REQUEST = 1337

    private fun hasPermission(perm: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm)
        } else {
            TODO("VERSION.SDK_INT < M")
            true
        }
    }

    private fun canAccessLocation(): Boolean {
        return hasPermission(ACCESS_FINE_LOCATION)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, CustomerProfileActivity::class.java)
            startActivity(intent)
        }

        var nearbyTruckButton: Button = findViewById(R.id.nearby_truck_button)
        nearbyTruckButton.setOnClickListener {
            val intent = Intent(this, CustomerNearbyTruckActivity::class.java)
            intent.putExtra("customerId", this.intent.getStringExtra("customerId"))
            startActivity(intent)
        }

        ordersButton.setOnClickListener {
            val intent = Intent(this, CustomerOrderHistoryActivity::class.java)
            intent.putExtra("customerId", this.intent.getStringExtra("customerId"))
            startActivity(intent)
        }

        Log.d(TAG, "onCreate() called")

        // grant permissions
        if (!canAccessLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
            }
        }

        // update user location
        UserLocation.updateLocation(this)


    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    public override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    public override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}
