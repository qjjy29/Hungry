package com.osueat.hungry.ui.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_customer_order.*
import java.util.*
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.osueat.hungry.R
import com.osueat.hungry.notification.NotificationSender
import kotlinx.android.synthetic.main.layout_add_food_to_order.view.*


class CustomerOrderActivity : AppCompatActivity() {

    private val TAG = "CustomerOrderActivity"
    private val ref = FirebaseDatabase.getInstance().reference
    private val orderDao = OrderDao(ref)
    private var foodList = ArrayList<Food>()
    private val foodDao = FoodDao(ref)

    private var currentOrderList = ArrayList<OrderedFood>()
    private var foodInOrderList = ArrayList<Food>()

    // variables for screen rotation
    private var addToOrderWindowOpen = false
    private var foodIndex = 0

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

                // create a new order
                val order = Order(UUID.randomUUID().toString(), intent.getStringExtra("customerId"), intent.getStringExtra("vendorId"),
                    intent.getStringExtra("truckId"), currentOrderList, "ORDER RECEIVED", totalPrice, null, Calendar.getInstance().time, Calendar.getInstance().time)
                
                orderDao.createOrder(order)

                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_LONG).show()
                // create a listener for this order
                val notificationSender = NotificationSender(this)

                ref.child("orders").child(order.id).
                    addChildEventListener(object: ChildEventListener {
                        override fun onChildRemoved(p0: DataSnapshot) {

                        }

                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                            val status = dataSnapshot.value.toString()
                            Log.d(TAG, status)
                            if (status == "IN PROGRESS") {
                                notificationSender.sendNotification(notificationSender.ORDER_IN_PROGRESS_TITLE,
                                    notificationSender.ORDER_IN_PROGRESS_CONTENT)
                            }

                            if (status == "ORDER READY") {
                                notificationSender.sendNotification(notificationSender.ORDER_READY_TITLE,
                                    notificationSender.ORDER_READY_CONTENT)
                            }

                            if (status == "ORDER CANCELED") {
                                notificationSender.sendNotification(notificationSender.ORDER_CANCELED_TITLE,
                                    notificationSender.ORDER_CANCELED_CONTENT)
                            }
                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }



                    })

                //after making an order, clear the current order list
                currentOrderList.clear()
            }
        })

        cancelButton.setOnClickListener(View.OnClickListener {
            finish()
        })

        findViewById<ListView>(R.id.menuListView).setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val f = foodList[i]
            foodIndex = i
            createAddToOrderWindow(f, null)
        })
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putBoolean("addToOrderWindowOpen", addToOrderWindowOpen)
        bundle.putParcelableArrayList("currentOrderList", currentOrderList)
        bundle.putParcelableArrayList("foodInOrderList", foodInOrderList)
        bundle.putParcelableArrayList("foodList", foodList)
        bundle.putInt("foodIndex", foodIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")

        currentOrderList = savedInstanceState!!.getParcelableArrayList<OrderedFood>("currentOrderList")!!
        foodInOrderList = savedInstanceState!!.getParcelableArrayList<Food>("foodInOrderList")!!
        foodList = savedInstanceState!!.getParcelableArrayList<Food>("foodList")!!

        if (savedInstanceState!!.getBoolean("addToOrderWindowOpen")) {
            createAddToOrderWindow(foodList[savedInstanceState!!.getInt("foodIndex")], savedInstanceState)
        }
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

    private fun createAddToOrderWindow(food: Food, restoredValues : Bundle?) {
        addToOrderWindowOpen = true
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
            addToOrderWindowOpen = false
        })

        updateView.cancelButton.setOnClickListener(View.OnClickListener {
            alertWindow.dismiss()
            addToOrderWindowOpen = falseAQ
        })
    }
}
