package com.osueat.hungry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.model.*

class CustomerOrderHistoryActivity : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance().reference
    private val TAG = "OrderHistoryActivity"
    private val orderList = ArrayList<Order>()
    private val orderDao = OrderDao(ref)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order_history)
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.child("orders").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                orderList.clear()

                for (o in dataSnapshot.children) {
                    val order = orderDao.constructOrderByHashMap(o)

                    if (order.customerId == intent.getStringExtra("customerId")) {
                        orderList.add(order)
                    }
                }

                val orderListAdapter =
                    OrderListAdapter(this@CustomerOrderHistoryActivity, orderList)
                findViewById<ListView>(R.id.orderListView).adapter = orderListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }
}
