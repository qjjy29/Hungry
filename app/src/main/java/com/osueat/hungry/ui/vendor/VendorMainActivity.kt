package com.osueat.hungry.ui.vendor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_vendor_main.*
import kotlinx.android.synthetic.main.fragment_vendor_main.*
import android.content.Intent
import android.util.Log.d
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.Truck
import com.osueat.hungry.model.TruckDao
import com.osueat.hungry.model.TruckListAdapter
import java.util.*
import kotlin.collections.HashMap

class VendorMainActivity : AppCompatActivity() {

    private val TAG = "VendorMainActivity"

    val truckList = ArrayList<Truck>()
    private val ref = FirebaseDatabase.getInstance().reference.child("trucks")

    private val tempFoodIdList = ArrayList<String>()
    private val truckDao = TruckDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.osueat.hungry.R.layout.activity_vendor_main)
        setSupportActionBar(toolbar)

        addTruckActivityButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VendorAddTruckActivity::class.java)
            intent.putExtra("vendorId", this.intent.getStringExtra("vendorId"))
            startActivity(intent)
        })

        findViewById<ListView>(R.id.truckListView).setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val t = truckList.get(i)

            val intent = Intent(this, VendorTruckActivity::class.java)
            intent.putExtra("truckId", t.id)
            intent.putExtra("vendorId", this.intent.getStringExtra("vendorId"))
            startActivity(intent)
        })

        Log.d(TAG, "onCreate() called")
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                truckList.clear()

                for (t in dataSnapshot.children) {
                    val truck = truckDao.constructTruckByHashMap(t)

                    if (truck.vendorId == intent.getStringExtra("vendorId")) {
                        truckList.add(truck)
                    }
                }

                val truckListAdapter = TruckListAdapter(this@VendorMainActivity, truckList)
                findViewById<ListView>(R.id.truckListView).adapter = truckListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
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
