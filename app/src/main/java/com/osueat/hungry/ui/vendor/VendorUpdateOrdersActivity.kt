package com.osueat.hungry.ui.vendor

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.Order
import com.osueat.hungry.model.OrderDao
import com.osueat.hungry.model.OrderListAdapter
import kotlinx.android.synthetic.main.layout_update_order_status.view.*
import java.util.*
import kotlin.collections.ArrayList

class VendorUpdateOrdersActivity : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance().reference
    private val TAG = "UpdateOrdersActivity"
    private val orderList = ArrayList<Order>()
    private val orderDao = OrderDao(ref)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_update_orders)

        findViewById<ListView>(R.id.orderListView).setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val o = orderList[i]
            createUpdateOrderStatusWindow(o)
        })
    }

    private fun createUpdateOrderStatusWindow(order: Order) {
        val alertDialog = AlertDialog.Builder(this@VendorUpdateOrdersActivity)
        val updateView = layoutInflater.inflate(R.layout.layout_update_order_status, null)
        alertDialog.setView(updateView)
        alertDialog.setTitle(order.id)

        // show update/delete window
        val alertWindow = alertDialog.create()
        alertWindow.show()

        updateView.updateStatusButton.setOnClickListener(View.OnClickListener {
            val newStatus = updateView.statusEditText.text.toString()

            // update name, address, and status of truck
            if (!TextUtils.isEmpty(newStatus)) {
                val newOrder = Order(order.id, order.customerId, order.vendorId, order.truckId, order.orderedFoodList,
                    newStatus, order.price, null, order.createDate, Calendar.getInstance().time)

                orderDao.updateOrderById(order.id, newOrder)

                Toast.makeText(this, "Order status updated", Toast.LENGTH_LONG).show()
                alertWindow.dismiss()
            }
            else {
                Toast.makeText(this, "Please enter a new status", Toast.LENGTH_LONG).show()
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.child("orders").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                orderList.clear()

                for (o in dataSnapshot.children) {
                    val order = orderDao.constructOrderByHashMap(o)

                    if (order.vendorId == intent.getStringExtra("vendorId")) {
                        orderList.add(order)
                    }
                }

                val orderListAdapter = OrderListAdapter(this@VendorUpdateOrdersActivity, orderList)
                findViewById<ListView>(R.id.orderListView).adapter = orderListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }
}
