package com.osueat.hungry.model

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.osueat.hungry.R

class FoodListAdapter(private val context: Activity, internal var foodList: List<Food>) :
    ArrayAdapter<Food>(context, R.layout.layout_food_list, foodList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_food_list, null, true)

        // add food name/price to list item
        val nameTextView = listViewItem.findViewById(R.id.nameTextView) as TextView
        val priceTextView = listViewItem.findViewById(R.id.priceTextView) as TextView
        val food = foodList[position]
        nameTextView.text = food.name
        priceTextView.text = food.price.toString()

        return listViewItem
    }
}