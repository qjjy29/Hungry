package com.osueat.hungry.ui.vendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.Truck
import kotlinx.android.synthetic.main.activity_vendor_add_truck.*

class VendorAddTruckActivity : AppCompatActivity() {

    private val TAG = "VendorAddTruckActivity"

    val truckList = ArrayList<Truck>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_truck)

        val ref = FirebaseDatabase.getInstance().getReference().child("trucks")


        saveButton.setOnClickListener(View.OnClickListener {
            val name = nameEditText.text
            val address = addressEditText.text
            val t = Truck(name.toString(), address.toString())
            truckList.add(t)

            ref.push().setValue(t)
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
