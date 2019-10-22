package com.osueat.hungry.ui.vendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.Truck
import kotlinx.android.synthetic.main.activity_vendor_add_truck.*

class VendorAddTruckActivity : AppCompatActivity() {


    private val TAG = "VendorAddTruckActivity"

    val truckList = ArrayList<Truck>()
    val ref = FirebaseDatabase.getInstance().reference.child("trucks")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_truck)

        saveButton.setOnClickListener(View.OnClickListener {
            val name = nameEditText.text
            val address = addressEditText.text

            // if truck name/address is provided, add to database
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
                val id = ref.push().key
                val truck = Truck(id.toString(), name.toString(), address.toString())

                // add truck to database
                ref.child(id.toString()).setValue(truck)
                truckList.add(truck)

                // reset edit texts
                nameEditText.setText("")
                addressEditText.setText("")

                Toast.makeText(applicationContext, "Added entry for truck to database", Toast.LENGTH_LONG).show()
            } else {
                if (name.toString() == "")
                    Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show()
                else if (address.toString() == "")
                    Toast.makeText(this, "Please enter an address", Toast.LENGTH_LONG).show()
            }
        })
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    public override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    public override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    public override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}
