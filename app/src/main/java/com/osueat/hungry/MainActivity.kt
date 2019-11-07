package com.osueat.hungry

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.solver.widgets.ChainHead
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.model.*
import com.osueat.hungry.notification.NotificationHandler
import java.util.*
import kotlin.collections.HashMap
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import com.osueat.hungry.data.model.CurrentCustomer
import com.osueat.hungry.data.model.CurrentUser
import com.osueat.hungry.notification.NotificationSender
import com.osueat.hungry.services.gms.UserLocation
import kotlinx.android.synthetic.main.activity_customer_order.*
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
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        var button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            val b = Bundle()
            b.putDouble("lat", 40.002) //latitude of the truck
            b.putDouble("lng", -83.018) //longitude of the truck
            b.putString("name", "All you can eat truck")
            intent.putExtras(b) //Put your id to your next Intent
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

        // test orderedfood
        val orderedFood1 = OrderedFood("1", 1)
        val orderedFood2 = OrderedFood("2", 1)
        val orderedFoodList = listOf(orderedFood1, orderedFood2)
        val id = "13"
        val customerId = "15"
        val vendorId = "18"
        val truckId = "213"
        val status = "Pending"
        val price = 6.0
        val createDate = Date()
        val lastUpdateDate = Date()
        val order = Order(
            id, customerId, vendorId, truckId, orderedFoodList,
            status, price, createDate, lastUpdateDate
        )

        val orderDao = OrderDao(ref)
        orderDao.createOrder(order)


        ref.child("orders").child(id).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val result = dataSnapshot.value
                Log.d(TAG, result.toString())
                if (result != null) {
                    val order = orderDao.constructOrderByHashMap(dataSnapshot)
                    Log.d(TAG, order.toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })

        // test codes for notification
        val notificationSender = NotificationSender(this)
        notificationSender.sendNotification("Order Prepared", "Your order is prepared. Enjoy your food soon!")
        notificationSender.sendNotification("Order Cancelled", "Your order has been cancelled successfully.")


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
