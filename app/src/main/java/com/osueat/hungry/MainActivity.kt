package com.osueat.hungry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.model.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

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
            var intent = Intent(this, TruckActivity::class.java)
            startActivity(intent)
        }

        Log.d(TAG, "onCreate() called")

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
        val ref = FirebaseDatabase.getInstance().reference
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
