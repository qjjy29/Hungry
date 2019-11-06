package com.osueat.hungry.ui.vendor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.R
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_vendor_truck.*
import kotlinx.android.synthetic.main.layout_update_delete_truck.view.*
import java.util.*
import kotlin.collections.HashMap
import com.google.firebase.database.DatabaseReference
import com.osueat.hungry.services.gms.UserLocation
import kotlinx.android.synthetic.main.activity_vendor_truck.view.*
import kotlinx.android.synthetic.main.layout_add_food_to_truck.view.*
import kotlinx.android.synthetic.main.layout_update_delete_truck.view.nameEditText
import kotlinx.android.synthetic.main.layout_update_delete_truck.view.updateTruckButton
import java.util.Calendar


class VendorTruckActivity : AppCompatActivity() {

    private val TAG = "VendorTruckActivity"

    private val foodList = ArrayList<Food>()
    private val ref = FirebaseDatabase.getInstance().reference

    private val foodDao = FoodDao(ref)
    private val truckDao = TruckDao(ref)
    private val foodIdList = ArrayList<String>()

    // todo: change this probably
    private val currentTruck = ArrayList<Truck>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_truck)

        foodIdList.add("")

        //Toast.makeText(this, this.intent.getStringExtra("truckId"), Toast.LENGTH_LONG).show()

        editTruckButton.setOnClickListener(View.OnClickListener {
            createUpdateTruckWindow(currentTruck[0].id, currentTruck[0].name, currentTruck[0].address, currentTruck[0].isActive)
        })

        addFoodButton.setOnClickListener(View.OnClickListener {
            createAddFoodWindow()
        })
    }

    private fun createUpdateTruckWindow(truckId: String, truckName: String, truckAddress: String, truckActiveStatus : Boolean) {
        val alertDialog = android.app.AlertDialog.Builder(this@VendorTruckActivity)
        val updateView = layoutInflater.inflate(R.layout.layout_update_delete_truck, null)
        alertDialog.setView(updateView)
        alertDialog.setTitle(truckName)

        // show update/delete window
        val alertWindow = alertDialog.create()
        alertWindow.show()

        updateView.updateTruckButton.setOnClickListener(View.OnClickListener {
            val newName = updateView.nameEditText.text.toString()
            val newAddress = updateView.addressEditText.text.toString()
            val isActive = updateView.activeCheckbox.isChecked

            // update name, address, and status of truck
            if (!TextUtils.isEmpty(newName) && !TextUtils.isEmpty(newAddress) && isActive == !truckActiveStatus) {
                updateTruck(truckId, newName, newAddress, isActive)
                alertWindow.dismiss()
                Toast.makeText(this, "Truck name, address, and status updated", Toast.LENGTH_LONG).show()
            }

            else if (TextUtils.isEmpty((newName)) || TextUtils.isEmpty((newAddress)) || isActive == !truckActiveStatus) {
                // update only the address; name remains unchanged
                if (TextUtils.isEmpty((newName)) && !TextUtils.isEmpty((newAddress))) {
                    updateTruck(truckId, truckName, newAddress, isActive)
                    Toast.makeText(this, "Truck address updated", Toast.LENGTH_LONG).show()
                    alertWindow.dismiss()
                }

                // update only name; address remains unchanged
                else if (!TextUtils.isEmpty((newName)) && TextUtils.isEmpty((newAddress))) {
                    updateTruck(truckId, newName, truckAddress, isActive)
                    Toast.makeText(this, "Truck name updated", Toast.LENGTH_LONG).show()
                    alertWindow.dismiss()
                }

                // update only active status of truck
                else {
                    updateTruck(truckId, truckName, truckAddress, isActive)
                    Toast.makeText(this, "Truck status updated", Toast.LENGTH_LONG).show()
                }
            }
        })

        updateView.deleteTruckButton.setOnClickListener(View.OnClickListener {
            truckDao.deleteTruckById(truckId)
            Toast.makeText(this, "Truck deleted from database", Toast.LENGTH_LONG).show()
            finish()
            alertWindow.dismiss()
        })
    }

    private fun createAddFoodWindow() {
        val alertDialog = AlertDialog.Builder(this@VendorTruckActivity)
        val addFoodView = layoutInflater.inflate(R.layout.layout_add_food_to_truck, null)
        alertDialog.setView(addFoodView)
        alertDialog.setTitle("New Food")
        val alertWindow = alertDialog.create()
        alertWindow.show()

        addFoodView.addNewFoodButton.setOnClickListener (View.OnClickListener {

            val name = addFoodView.nameEditText.text.toString()
            val price = addFoodView.priceEditText.text.toString()
            val description = addFoodView.descriptionEditText.text.toString()

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Please enter a name for the food", Toast.LENGTH_LONG).show()
            }

            else if (TextUtils.isEmpty(price)) {
                Toast.makeText(this, "Please enter a price for the food", Toast.LENGTH_LONG).show()
            }

            else if (TextUtils.isEmpty(description)) {
                Toast.makeText(this, "Please enter a description for the food", Toast.LENGTH_LONG).show()
            }

            else {
                val food = Food(UUID.randomUUID().toString(), this.intent.getStringExtra("truckId"),
                    name, price.toDouble(), description, Calendar.getInstance().time, Calendar.getInstance().time)

                if (foodIdList[0] == "") {
                    foodIdList[0] = food.id
                } else {
                    foodIdList.add(food.id)
                }

                // add food id to truck
                ref.child("trucks").child(this.intent.getStringExtra("truckId")).child("foodIdList").setValue(foodIdList)

                foodDao.createFood(food)
                foodList.add(food)
                alertWindow.dismiss()
            }
        })


        addFoodView.cancelButton.setOnClickListener (View.OnClickListener {
            alertWindow.dismiss()
        })
    }

    private fun updateTruck(truckId: String, newName: String, newAddress: String, isActive : Boolean) {
        // TODO: change food list
        val truckLocation = UserLocation.getLocation()
        val newTruck = Truck(truckId, newName, newAddress, foodIdList, intent.getStringExtra("vendorId"),
            isActive, truckLocation.latitude, truckLocation.longitude)
        truckDao.updateTruckById(truckId, newTruck)
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        // truck listener
        val truckRef = FirebaseDatabase.getInstance().reference.child("trucks").child(intent.getStringExtra("truckId"))
        truckRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // update text for truck name and address
                val truck = truckDao.constructTruckByHashMap(dataSnapshot)
                truckName.text = truck.name
                truckAddress.text = truck.address

                currentTruck.clear()
                currentTruck.add(truck)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })

        // menu listener
        ref.child("foods").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                foodList.clear()

                for (f in dataSnapshot.children) {
                    val food = foodDao.constructFoodByHashMap(f)

                    if (food.truckId == intent.getStringExtra("truckId")) {
                        foodList.add(food)
                    }
                }

                val foodListAdapter = FoodListAdapter(this@VendorTruckActivity, foodList)
                findViewById<ListView>(R.id.menuListView).adapter = foodListAdapter
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
