package com.osueat.hungry.ui.customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.data.model.CurrentCustomer
import com.osueat.hungry.model.*
import java.util.*
import kotlin.collections.ArrayList

class CustomerOrderDetailActivity : AppCompatActivity() {

    private val TAG = "OrderDetailActivity"
    private val ref = FirebaseDatabase.getInstance().reference
    private val orderList = ArrayList<Order>()
    private val orderDao = OrderDao(ref)
    private var orderId = "123"

    private var payButton: Button? = null
    private var cancelButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_order_detail)

        val b: Bundle? = intent.extras

        if (b != null) {
            orderId = b.getString("orderId")!!
        }

        payButton = findViewById<Button?>(R.id.payButton)
        cancelButton = findViewById<Button?>(R.id.cancelButton)

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called $orderId")

        ref.child("orders").orderByChild("id").equalTo(orderId)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                orderList.clear()

                for (o in dataSnapshot.children) {
                    val order = orderDao.constructOrderByHashMap(o)

                    orderList.add(order)

                    if (order.paymentId != null || order.status == "ORDER CANCELED") {
                        payButton?.visibility = View.INVISIBLE
                        cancelButton?.visibility = View.INVISIBLE
                    } else {
                        payButton?.setOnClickListener{
                            val customer: Customer? = CurrentCustomer.getCustomer()
                            if (customer != null) {
                                if (order.price <= customer.balance) {
                                    // create a payment
                                    val paymentDao = PaymentDao(ref)
                                    val payment = Payment(UUID.randomUUID().toString(), customer.id,
                                        order.vendorId, order.id, order.price, Date(), Date())
                                    paymentDao.createPayment(payment)
                                    // pay the money from the customer's balance
                                    val customerDao = CustomerDao(ref)
                                    val updatedCustomer = Customer(customer.id, customer.userId,
                                        customer.nickname, customer.balance - order.price)
                                    customerDao.updateCustomerById(customer.id, updatedCustomer)
                                    // update the paymentId to the order
                                    val orderDao = OrderDao(ref)
                                    val updatedOrder = Order(order.id, order.customerId, order.vendorId,
                                        order.truckId, order.orderedFoodList, order.status, order.price,
                                        payment.id, order.createDate, Date())
                                    orderDao.updateOrderById(order.id, updatedOrder)
                                    Toast.makeText(this@CustomerOrderDetailActivity,
                                        "You have made the payment successfully!", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(this@CustomerOrderDetailActivity,
                                        "Cannot make a payment: insufficient balance.", Toast.LENGTH_LONG).show()
                                }
                            }

                        }
                    }

                }

                val orderListAdapter =
                    OrderListAdapter(this@CustomerOrderDetailActivity, orderList)
                findViewById<ListView>(R.id.orderListView).adapter = orderListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }
}
