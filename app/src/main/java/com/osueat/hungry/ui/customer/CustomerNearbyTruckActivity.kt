package com.osueat.hungry.ui.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class CustomerNearbyTruckActivity : AppCompatActivity() {

    private val ref = FirebaseDatabase.getInstance().reference
    private val TAG = "NearbyTruckActivity"

    val truckList = ArrayList<Truck>()
    private val truckDao = TruckDao(ref)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_nearby_truck)

        findViewById<ListView>(R.id.truckListView).setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val t = truckList[i]

            val intent = Intent(this, CustomerTruckInfoActivity::class.java)
            intent.putExtra("foodIdList", t.foodIdList)
            intent.putExtra("truckId", t.id)
            intent.putExtra("truckName", t.name)
            intent.putExtra("vendorId", t.vendorId)
            intent.putExtra("customerId", this.intent.getStringExtra("customerId"))
            intent.putExtra("truckLatitude", t.latitude)
            intent.putExtra("truckLongitude", t.longitude)
            startActivity(intent)
        })
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.child("trucks").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                truckList.clear()

                for (t in dataSnapshot.children) {
                    val truck = truckDao.constructTruckByHashMap(t)

                    if (truck.isActive) {
                        truckList.add(truck)
                    }
                }

                val truckListAdapter = TruckListAdapter(this@CustomerNearbyTruckActivity, truckList)
                findViewById<ListView>(R.id.truckListView).adapter = truckListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }
}
