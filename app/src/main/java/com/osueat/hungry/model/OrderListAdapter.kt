package com.osueat.hungry.model

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.osueat.hungry.R
import com.google.firebase.database.ValueEventListener






class OrderListAdapter(private val context: Activity, private var orderList: List<Order>) :
    ArrayAdapter<Order>(context, R.layout.layout_order_list, orderList) {

    private val ref = FirebaseDatabase.getInstance().reference

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_order_list, null, true)

        val order = orderList[position]

        val nameTextView = listViewItem.findViewById(R.id.nameTextView) as TextView
        val orderDateTextView = listViewItem.findViewById(R.id.orderDateTextView) as TextView
        val statusTextView = listViewItem.findViewById(R.id.statusTextView) as TextView

        //todo: change truck id to the truck's name


        //var truckName = order.truckId

        ref.child("trucks").child(order.truckId).child("name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                nameTextView.text = snapshot.value.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        //nameTextView.text = truckName
        orderDateTextView.text = order.createDate.toString()
        statusTextView.text = order.status

        return listViewItem
    }

    override fun getItem(position: Int): Order? {
        return orderList[position]
    }
}