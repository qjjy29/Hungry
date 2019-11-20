package com.osueat.hungry.model

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.osueat.hungry.R



class OrderListAdapter(private val context: Activity, private var orderList: List<Order>) :
    ArrayAdapter<Order>(context, R.layout.layout_order_list, orderList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_order_list, null, true)

        val order = orderList[position]

        val nameTextView = listViewItem.findViewById(R.id.nameTextView) as TextView
        val orderDateTextView = listViewItem.findViewById(R.id.orderDateTextView) as TextView
        val statusTextView = listViewItem.findViewById(R.id.statusTextView) as TextView

        //todo: change truck id to the truck's name
        nameTextView.text = order.truckId
        orderDateTextView.text = order.createDate.toString()
        statusTextView.text = order.status

        return listViewItem
    }

    override fun getItem(position: Int): Order? {
        return orderList[position]
    }
}