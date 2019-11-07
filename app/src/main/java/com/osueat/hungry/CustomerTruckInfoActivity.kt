package com.osueat.hungry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_customer_truck_info.*
import kotlinx.android.synthetic.main.activity_vendor_add_truck.*
import kotlinx.android.synthetic.main.activity_vendor_truck.*
import java.util.*

class CustomerTruckInfoActivity : AppCompatActivity() {

    private val TAG = "TruckInfoActivity"

    private val foodList = ArrayList<Food>()
    private val ref = FirebaseDatabase.getInstance().reference
    private val foodDao = FoodDao(ref)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_truck_info)

        //var truckNameText: ViewText = findViewById(R.id.truckNameText)

        createOrderButton.setOnClickListener(View.OnClickListener {
            val orderIntent = Intent(this, CustomerOrderActivity::class.java)
            orderIntent.putExtra("truckId", intent.getStringExtra("truckId"))
            orderIntent.putExtra("vendorId", this.intent.getStringExtra("vendorId"))
            orderIntent.putExtra("customerId", this.intent.getStringExtra("customerId"))
            startActivity(orderIntent)
        })
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
