package com.osueat.hungry.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.MapsActivity
import com.osueat.hungry.R
import com.osueat.hungry.model.Food
import com.osueat.hungry.model.FoodDao
import com.osueat.hungry.model.FoodListAdapter

import java.util.ArrayList

class CustomerTruckInfoActivity : AppCompatActivity() {

    private val TAG = "TruckInfoActivity"

    private val foodList = ArrayList<Food>()
    private val ref = FirebaseDatabase.getInstance().reference
    private val foodDao = FoodDao(ref)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_truck_info)
        val viewMapButton = findViewById<Button>(R.id.viewMapButton)
        viewMapButton.setOnClickListener {
            var newIntent = Intent(this, MapsActivity::class.java)
            val b = Bundle()
            b.putDouble("lat", intent.getDoubleExtra("truckLatitude", 0.0)) //latitude of the truck
            b.putDouble("lng", intent.getDoubleExtra("truckLongitude", 0.0)) //longitude of the truck
            b.putString("name", intent.getStringExtra("truckName"))

            newIntent.putExtras(b) //Put your id to your next Intent
            startActivity(newIntent)
        }
        //var truckNameText: ViewText = findViewById(R.id.truckNameText)
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        // menu listener
        ref.child("foods").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodList.clear()

                for (f in dataSnapshot.children) {
                    val food = foodDao.constructFoodByHashMap(f)

                    if (food.truckId == intent.getStringExtra("truckId")) {
                        foodList.add(food)
                    }
                }

                val foodListAdapter = FoodListAdapter(this@CustomerTruckInfoActivity, foodList)
                findViewById<ListView>(R.id.menuListView).adapter = foodListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }
}
