package com.osueat.hungry.ui.vendor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.util.Log.d
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.Truck
import com.osueat.hungry.model.TruckDao
import java.util.*
import kotlin.collections.HashMap

class VendorRetrieveTruckListActivity : AppCompatActivity() {

    var truckList: MutableList<Truck> = mutableListOf()
    val TAG = "VendorRetrieveTruckList"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_retrieve_truck_list)
        

        val ref = FirebaseDatabase.getInstance().reference.child("trucks")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()) {
                    for (t in p0.children) {
                        val truck = t.value as HashMap<String, Objects>
                        val truckDao = TruckDao()
                        truckList.add(truckDao.constructTruckByHashMap(truck))
                    }
                }

                // print list of trucks in database
                for (t in truckList) {
                    Log.d(TAG, t.getName() + ", " + t.getAddress())
                }
            }
        })


    }
}
