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
import com.osueat.hungry.services.gms.UserLocation
import kotlinx.android.synthetic.main.activity_vendor_add_truck.addressEditText
import kotlinx.android.synthetic.main.activity_vendor_add_truck.nameEditText
import kotlinx.android.synthetic.main.layout_update_delete_truck.*
import kotlinx.android.synthetic.main.layout_update_delete_truck.view.*
import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton



class VendorAddTruckActivity : AppCompatActivity() {

    private val TAG = "VendorAddTruckActivity"

    var truckList = ArrayList<Truck>()
    private val ref = FirebaseDatabase.getInstance().reference

    private val tempFoodIdList = ArrayList<String>()
    private val truckDao = TruckDao(ref)

    // values stored if screen is rotated
    private var alertWindowOpen = false
    private var truckIndex = 0
    private var previousName = ""
    private var previousAddress = ""
    private var previousChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_add_truck)

        tempFoodIdList.add("")

        // add truck to database when save button pressed
        saveButton.setOnClickListener(View.OnClickListener {
            val name = nameEditText.text
            val address = addressEditText.text

            // if truck name/address is provided, add to database
            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(address)) {
                // TODO: change food list and vendor id
                val truckLocation = UserLocation.getLocation()
                val truck = Truck(UUID.randomUUID().toString(), name.toString(), address.toString(),
                    tempFoodIdList, this.intent.getStringExtra("vendorId"), false,
                    truckLocation.latitude, truckLocation.longitude)

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
            truckIndex = i
            createUpdateTruckWindow(t.id, t.name, t.address, t.isActive, null)
        })
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putBoolean("alertWindowOpen", alertWindowOpen)
        bundle.putInt("truckIndex", truckIndex)
        bundle.putParcelableArrayList("truckList", truckList)
        bundle.putString("previousName", previousName)
        bundle.putString("previousAddress", previousAddress)
        bundle.putBoolean("previousChecked", previousChecked)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")

        if (savedInstanceState!!.getBoolean("alertWindowOpen") == true) {
            truckList = savedInstanceState!!.getParcelableArrayList<Truck>("truckList")!!
            val t = truckList.get(savedInstanceState!!.getInt("truckIndex"))
            createUpdateTruckWindow(t.id, t.name, t.address, t.isActive, savedInstanceState)
        }
    }

    private fun createUpdateTruckWindow(truckId: String, truckName: String, truckAddress: String, truckActiveStatus : Boolean, restoredValues : Bundle?) {
        alertWindowOpen = true
        val alertDialog = AlertDialog.Builder(this@VendorAddTruckActivity)
        val updateView = layoutInflater.inflate(R.layout.layout_update_delete_truck, null)
        alertDialog.setView(updateView)
        alertDialog.setTitle(truckName)

        alertDialog.setOnCancelListener {
            alertWindowOpen = false
            previousName = ""
            previousAddress = ""
            previousChecked = false
        }

        // show update/delete window
        val alertWindow = alertDialog.create()
        alertWindow.show()

        if (restoredValues != null) {
            previousName = restoredValues!!.getString("previousName").toString()
            previousAddress = restoredValues!!.getString("previousAddress").toString()
            previousChecked = restoredValues!!.getBoolean("previousChecked")

            updateView.nameEditText.setText(restoredValues!!.getString("previousName"))
            updateView.addressEditText.setText(restoredValues!!.getString("previousAddress"))
            updateView.activeCheckbox.isChecked = restoredValues!!.getBoolean("previousChecked")
        }

        updateView.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                previousName = updateView.nameEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
        })

        updateView.addressEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                previousAddress = updateView.addressEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
        })

        updateView.activeCheckbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            previousChecked = updateView.activeCheckbox.isChecked
        })


        updateView.updateTruckButton.setOnClickListener(View.OnClickListener {
            val newName = updateView.nameEditText.text.toString()
            val newAddress = updateView.addressEditText.text.toString()
            val isActive = updateView.activeCheckbox.isChecked

            // update name, address, and status of truck
            if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newAddress) && isActive == !truckActiveStatus) {
                updateTruck(truckId, newName, newAddress, isActive)
                alertWindow.dismiss()
                alertWindowOpen = false
                Toast.makeText(this, "Truck name, address, and status updated", Toast.LENGTH_LONG).show()
                previousName = ""
                previousAddress = ""
                previousChecked = false
            }

            else if (TextUtils.isEmpty((newName)) || TextUtils.isEmpty((newAddress)) || isActive == !truckActiveStatus) {
                // update only the address; name remains unchanged
                if (TextUtils.isEmpty((newName)) && !TextUtils.isEmpty((newAddress))) {
                    updateTruck(truckId, truckName, newAddress, isActive)
                    Toast.makeText(this, "Truck address updated", Toast.LENGTH_LONG).show()
                    alertWindow.dismiss()
                    alertWindowOpen = false
                    previousName = ""
                    previousAddress = ""
                    previousChecked = false
                }

                // update only name; address remains unchanged
                else if (!TextUtils.isEmpty((newName)) && TextUtils.isEmpty((newAddress))) {
                    updateTruck(truckId, newName, truckAddress, isActive)
                    Toast.makeText(this, "Truck name updated", Toast.LENGTH_LONG).show()
                    alertWindow.dismiss()
                    alertWindowOpen = false
                    previousName = ""
                    previousAddress = ""
                    previousChecked = false
                }

                // update only active status of truck
                else {
                    updateTruck(truckId, truckName, truckAddress, isActive)
                    alertWindowOpen = false
                    Toast.makeText(this, "Truck status updated", Toast.LENGTH_LONG).show()
                    previousName = ""
                    previousAddress = ""
                    previousChecked = false
                    alertWindow.dismiss()
                }
            }
        })

        updateView.deleteTruckButton.setOnClickListener(View.OnClickListener {
            truckDao.deleteTruckById(truckId)
            Toast.makeText(this, "Truck deleted from database", Toast.LENGTH_LONG).show()
            alertWindow.dismiss()
            alertWindowOpen = false
            previousName = ""
            previousAddress = ""
            previousChecked = false
        })
    }

    private fun updateTruck(truckId: String, newName: String, newAddress: String, isActive : Boolean) {
        // TODO: change food list and vendor id
        val truckLocation = UserLocation.getLocation()
        val newTruck = Truck(truckId, newName, newAddress, tempFoodIdList,
            this.intent.getStringExtra("vendorId"), isActive,
            truckLocation.latitude, truckLocation.longitude)
        truckDao.updateTruckById(truckId, newTruck)
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        ref.child("trucks").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                truckList.clear()

                for (t in dataSnapshot.children) {
                    val truck = truckDao.constructTruckByHashMap(t)

                    if (truck.vendorId == intent.getStringExtra("vendorId")) {
                        truckList.add(truck)
                    }
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
