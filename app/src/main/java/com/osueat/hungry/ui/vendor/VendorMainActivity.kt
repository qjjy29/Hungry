package com.osueat.hungry.ui.vendor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_vendor_main.*
import kotlinx.android.synthetic.main.fragment_vendor_main.*
import android.content.Intent
import android.util.Log.d
import android.util.Log


class VendorMainActivity : AppCompatActivity() {

    private val TAG = "VendorMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.osueat.hungry.R.layout.activity_vendor_main)
        setSupportActionBar(toolbar)

        orderButton1.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        orderButton2.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        orderButton3.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        orderButton4.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        addTruckActivityButton.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, VendorAddTruckActivity::class.java))
        })

        // create a sample truck to add to the database
        //val db = FirebaseDatabase.getInstance().reference
        //val t = Truck("Sample Truck")
        //val truckDB = TruckDatabaseManager(db)

        //truckDB.createTruck(t)

        Log.d(TAG, "onCreate() called")
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
