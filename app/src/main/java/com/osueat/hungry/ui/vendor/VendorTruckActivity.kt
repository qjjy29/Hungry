package com.osueat.hungry.ui.vendor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.*
import java.util.*
import kotlin.collections.HashMap

class VendorTruckActivity : AppCompatActivity() {

    private val TAG = "VendorTruckActivity"

    val foodList = ArrayList<Food>()
    private val ref = FirebaseDatabase.getInstance().reference.child("food")

    private val foodDao = FoodDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_truck)
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodList.clear()

                for (f in dataSnapshot.children) {
                    val food = f.value as HashMap<String, Objects>
                    foodList.add(foodDao.constructFoodByHashMap(food))
                }

                val foodListAdapter = FoodListAdapter(this@VendorTruckActivity, foodList)
                findViewById<ListView>(R.id.menuListView).adapter = foodListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
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
