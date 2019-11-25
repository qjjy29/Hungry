package com.osueat.hungry.ui.customer

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
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast


class CustomerOrderHistoryActivity : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance().reference
    private val TAG = "OrderHistoryActivity"
    private val orderList = ArrayList<Order>()
    private val orderDao = OrderDao(ref)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order_history)
        val listView = findViewById<ListView>(R.id.orderListView)
        listView.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item = parent?.getItemAtPosition(position) as Order

                val b = Bundle()
                b.putString("orderId", item.id) //latitude of the truck

                val intent = Intent(this@CustomerOrderHistoryActivity,
                    CustomerOrderDetailActivity::class.java)

                intent.putExtras(b)
                //based on item add info to intent
                startActivity(intent)
            }
        })
    }

    private fun checkConnection() {
        ref.child(".info/connected").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val connected = dataSnapshot.getValue(Boolean::class.java)
                if (connected == null || !connected) {
                    Toast.makeText(this@CustomerOrderHistoryActivity,
                        "Connection lost", Toast.LENGTH_LONG).show()
                } else {
                    getOrderList()
                }
            }
        })
    }

    private fun getOrderList() {

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

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        checkConnection()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
}
