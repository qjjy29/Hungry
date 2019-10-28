package com.osueat.hungry.ui.vendor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Log.d
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import kotlinx.android.synthetic.main.activity_vendor_add_truck.*
import android.nfc.Tag
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import com.osueat.hungry.MainActivity
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.app.AlertDialog
import android.widget.*
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_vendor_add_truck.addressEditText
import kotlinx.android.synthetic.main.activity_vendor_add_truck.nameEditText
import kotlinx.android.synthetic.main.layout_update_delete_truck.*
import kotlinx.android.synthetic.main.layout_update_delete_truck.view.*


class VendorAddTruckActivity : AppCompatActivity() {

    private val TAG = "VendorAddTruckActivity"

    val truckList = ArrayList<Truck>()
    val ref = FirebaseDatabase.getInstance().reference.child("trucks")

    val tempFoodIdList = ArrayList<String>()
    val truckDao = TruckDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_truck)

        tempFoodIdList.add("testID_1")
        tempFoodIdList.add("testID_2")
        tempFoodIdList.add("testID_3")

        // add truck to database when save button pressed
        saveButton.setOnClickListener(View.OnClickListener {
            val name = nameEditText.text
            val address = addressEditText.text

            // if truck name/address is provided, add to database
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
                // TODO: change food list and vendor id
                val truck = Truck(UUID.randomUUID().toString(), name.toString(), address.toString(), tempFoodIdList, "TEMP")

                truckDao.createTruck(truck)
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

        // add on click listener to list view buttons to go to truck page
        findViewById<ListView>(R.id.truckListView).setOnItemClickListener(AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val t = truckList.get(i)
            createUpdateTruckWindow(t.id, t.name, t.address)
        })

    }

    private fun createUpdateTruckWindow(truckId: String, truckName: String, truckAddress: String) {
        val alertDialog = AlertDialog.Builder(this@VendorAddTruckActivity)
        val updateView = layoutInflater.inflate(R.layout.layout_update_delete_truck, null)
        alertDialog.setView(updateView)
        alertDialog.setTitle(truckName)

        // show update/delete window
        val alertWindow = alertDialog.create()
        alertWindow.show()

        updateView.updateTruckButton.setOnClickListener(View.OnClickListener {
            val newName = updateView.nameEditText.text.toString()
            val newAddress = updateView.addressEditText.text.toString()

            // update both name and address of truck
            if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newAddress)) {
                updateTruck(truckId, newName, newAddress)
                alertWindow.dismiss()
                Toast.makeText(this, "Truck name and address updated", Toast.LENGTH_LONG).show()
            }

            else if (TextUtils.isEmpty((newName)) || TextUtils.isEmpty((newAddress))) {
                // update only the address; name remains unchanged
                if (TextUtils.isEmpty((newName)) && !TextUtils.isEmpty((newAddress))) {
                    updateTruck(truckId, truckName, newAddress)
                    Toast.makeText(this, "Truck address updated", Toast.LENGTH_LONG).show()
                    alertWindow.dismiss()
                }

                // update only name; address remains unchanged
                else if (!TextUtils.isEmpty((newName)) && TextUtils.isEmpty((newAddress))) {
                    updateTruck(truckId, newName, truckAddress)
                    Toast.makeText(this, "Truck name updated", Toast.LENGTH_LONG).show()
                    alertWindow.dismiss()
                }

                else if (TextUtils.isEmpty(newName) && TextUtils.isEmpty(newAddress)) {
                    Toast.makeText(this, "Please enter a new name or address", Toast.LENGTH_LONG).show()
                }
            }
        })

        updateView.deleteTruckButton.setOnClickListener(View.OnClickListener {
            truckDao.deleteTruckById(truckId)
            Toast.makeText(this, "Truck deleted from database", Toast.LENGTH_LONG).show()
            alertWindow.dismiss()
        })
    }

    private fun updateTruck(truckId: String, newName: String, newAddress: String) {
        // TODO: change food list and vendor id
        val newTruck = Truck(truckId, newName, newAddress, tempFoodIdList, "TEMP")
        truckDao.updateTruckById(truckId, newTruck)
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                truckList.clear()

                for (t in dataSnapshot.children) {
                    val truck = t.value as HashMap<String, Objects>
                    val truckDao = TruckDao()
                    truckList.add(truckDao.constructTruckByHashMap(truck))
                }

                val truckListAdapter = TruckListAdapter(this@VendorAddTruckActivity, truckList)
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
