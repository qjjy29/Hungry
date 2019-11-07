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
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_customer_order.*
import kotlinx.android.synthetic.main.activity_vendor_add_truck.*
import java.util.*
import android.widget.CheckBox
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.layout_add_food_to_order.view.*


class CustomerOrderActivity : AppCompatActivity() {

    private val TAG = "CustomerOrderActivity"
    private val ref = FirebaseDatabase.getInstance().reference
    val orderDao = OrderDao(ref)
    private val foodList = ArrayList<Food>()
    private val foodDao = FoodDao(ref)

    private val currentOrderList = ArrayList<OrderedFood>()
    private val foodInOrderList = ArrayList<Food>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order)

        orderButton.setOnClickListener(View.OnClickListener {

            if (currentOrderList.size == 0) {
                Toast.makeText(this, "You must add food items to place an order", Toast.LENGTH_LONG).show()
            }

            else {
                // calculate total price of order
                var totalPrice = 0.0
                for (i in 0..foodInOrderList.size - 1) {
                    totalPrice += foodInOrderList[i].price
                }

                //TODO: Change ids
                val order = Order(UUID.randomUUID().toString(), intent.getStringExtra("customerId"), intent.getStringExtra("vendorId"),
                    intent.getStringExtra("truckId"), currentOrderList, "IN PROGRESS", totalPrice, Calendar.getInstance().time, Calendar.getInstance().time)
                orderDao.createOrder(order)

                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_LONG).show()
                //after making an order, clear the current order list
                currentOrderList.clear()
            }
        })

        cancelButton.setOnClickListener(View.OnClickListener {
            finish()
        })

        findViewById<ListView>(R.id.menuListView).setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val f = foodList[i]
            createAddToOrderWindow(f)
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

                val foodListAdapter = FoodListAdapter(this@CustomerOrderActivity, foodList)
                findViewById<ListView>(R.id.menuListView).adapter = foodListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }

    private fun createAddToOrderWindow(food: Food) {
        val alertDialog = AlertDialog.Builder(this@CustomerOrderActivity)
        val updateView = layoutInflater.inflate(R.layout.layout_add_food_to_order, null)
        alertDialog.setView(updateView)
        alertDialog.setTitle(food.name)

        // show update/delete window
        val alertWindow = alertDialog.create()
        alertWindow.show()

        updateView.addToOrderButton.setOnClickListener(View.OnClickListener {
            val orderedFood = OrderedFood(food.id, 1)
            currentOrderList.add(orderedFood)
            foodInOrderList.add(food)

            Toast.makeText(this, "Food added to order", Toast.LENGTH_SHORT).show()
            alertWindow.dismiss()
        })

        updateView.cancelButton.setOnClickListener(View.OnClickListener {
            alertWindow.dismiss()
        })
    }
}
