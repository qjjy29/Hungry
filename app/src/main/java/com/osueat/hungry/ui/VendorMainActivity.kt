package com.osueat.hungry.ui

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_vendor_main.*
import kotlinx.android.synthetic.main.fragment_vendor_main.*
import android.R.attr.button
import android.R
import android.content.Intent
import android.util.Log.d
import com.osueat.hungry.ui.login.VendorOrderActivity


class VendorMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.osueat.hungry.R.layout.activity_vendor_main)
        setSupportActionBar(toolbar)

        orderButton1.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        orderButton2.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        orderButton3.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })

        orderButton4.setOnClickListener(View.OnClickListener {
            d("Order Button", "Pressed order button.")
            startActivity(Intent(this, VendorOrderActivity::class.java))
        })
    }
}
