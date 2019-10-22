package com.osueat.hungry.model

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.osueat.hungry.R

class TruckListAdapter(private val context: Activity, internal var truckList: List<Truck>) :
    ArrayAdapter<Truck>(context, R.layout.layout_truck_list, truckList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_truck_list, null, true)

        // add truck name/address to list item
        val nameTextView = listViewItem.findViewById(R.id.nameTextView) as TextView
        val addressTextView = listViewItem.findViewById(R.id.addressTextView) as TextView
        val truck = truckList[position]
        nameTextView.text = truck.getName()
        addressTextView.text = truck.getAddress()

        return listViewItem
    }
}