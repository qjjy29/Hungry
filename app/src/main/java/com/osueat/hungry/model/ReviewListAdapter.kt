package com.osueat.hungry.model

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.osueat.hungry.R

class ReviewListAdapter(private val context: Activity, internal var reviewList: List<Review>):
    ArrayAdapter<Review>(context, R.layout.layout_review_list, reviewList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val listViewItem = inflater.inflate(R.layout.layout_review_list, null, true)

        // add review name/price to list item
        val nameTextView = listViewItem.findViewById<TextView>(R.id.nameTextView)
        val starTextView = listViewItem.findViewById<TextView>(R.id.starTextView)
        val contentTextView = listViewItem.findViewById<TextView>(R.id.contentTextView)
        val review = reviewList[position]
        nameTextView.text = review.customerNickname
        starTextView.text = review.star.toString()
        contentTextView.text = review.content

        return listViewItem
    }

}