package com.osueat.hungry.ui.customer

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.MapsActivity
import com.osueat.hungry.R
import com.osueat.hungry.data.model.CurrentCustomer
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_customer_truck_info.*
import kotlinx.android.synthetic.main.activity_customer_truck_info.view.addReviewButton
import kotlinx.android.synthetic.main.layout_add_review.view.*
import kotlinx.android.synthetic.main.layout_update_delete_truck.view.*
import java.util.*
import kotlin.collections.ArrayList


class CustomerTruckInfoActivity : AppCompatActivity() {

    private val TAG = "TruckInfoActivity"

    private val foodList = ArrayList<Food>()
    private var reviewList = ArrayList<Review>()
    private val ref = FirebaseDatabase.getInstance().reference
    private val foodDao = FoodDao(ref)
    private val reviewDao = ReviewDao(ref)

    // values stored if screen is rotated
    private var addReviewWindowOpen = false
    private var previousReviewText = ""
    private var previousScore = ""

    private fun addReviewWindow(truckName: String, restoredValues : Bundle?) {
        addReviewWindowOpen = true
        // add reviews
        val addReviewAlertDialog = AlertDialog.Builder(this@CustomerTruckInfoActivity)
        val addReviewView = layoutInflater.inflate(R.layout.layout_add_review, null)
        addReviewAlertDialog.setView(addReviewView)
        addReviewAlertDialog.setTitle(truckName)

        addReviewAlertDialog.setOnCancelListener {
            addReviewWindowOpen = false
            previousReviewText = ""
            previousScore = ""
        }

        // show add review window
        val alertWindow = addReviewAlertDialog.create()
        alertWindow.show()

        if (restoredValues != null) {
            previousReviewText = restoredValues!!.getString("previousReviewText").toString()
            previousScore = restoredValues!!.getString("previousScore").toString()

            addReviewView.reviewEditText.setText(restoredValues!!.getString("previousReviewText"))
            addReviewView.starEditText.setText(restoredValues!!.getString("previousScore"))
        }

        addReviewView.reviewEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                previousReviewText = addReviewView.reviewEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
        })

        addReviewView.starEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                previousScore = addReviewView.starEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
        })

        addReviewView.addReviewButton.setOnClickListener {
            // parameters
            val customer = CurrentCustomer.getCustomer()!!
            val id = UUID.randomUUID().toString()
            val customerId = customer.id
            val truckId = intent.getStringExtra("truckId")
            val customerNickname = customer.nickname
            val reviewContent = addReviewView.reviewEditText.text.toString()
            val star = addReviewView.starEditText.text.toString().toInt()
            val createDate = Date()
            val newReview = Review(id, customerId, truckId, customerNickname,
                reviewContent, star, createDate)
            reviewDao.createReview(newReview)
            Toast.makeText(applicationContext, "Thank you! Your review has been posted!", Toast.LENGTH_LONG).show()
            alertWindow.dismiss()
            addReviewWindowOpen = false
            previousReviewText = ""
            previousScore = ""
        }

        addReviewView.cancelReviewButton.setOnClickListener(View.OnClickListener {
            alertWindow.dismiss()
            addReviewWindowOpen = false
            previousReviewText = ""
            previousScore = ""
        })
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putBoolean("addReviewWindowOpen", addReviewWindowOpen)
        bundle.putParcelableArrayList("reviewList", reviewList)
        bundle.putString("previousReviewText", previousReviewText)
        bundle.putString("previousScore", previousScore)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")

        reviewList = savedInstanceState!!.getParcelableArrayList<Review>("reviewList")!!

        if (savedInstanceState!!.getBoolean("addReviewWindowOpen")) {
            addReviewWindow(intent.getStringExtra("truckName"), savedInstanceState)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_truck_info)
        val viewMapButton = findViewById<Button>(R.id.viewMapButton)
        viewMapButton.setOnClickListener {
            var newIntent = Intent(this, MapsActivity::class.java)
            val b = Bundle()
            b.putDouble("lat", intent.getDoubleExtra("truckLatitude", 0.0)) //latitude of the truck
            b.putDouble("lng", intent.getDoubleExtra("truckLongitude", 0.0)) //longitude of the truck
            b.putString("name", intent.getStringExtra("truckName"))

            newIntent.putExtras(b) //Put your id to your next Intent
            startActivity(newIntent)
        }
        //var truckNameText: ViewText = findViewById(R.id.truckNameText)

        createOrderButton.setOnClickListener(View.OnClickListener {
            val orderIntent = Intent(this, CustomerOrderActivity::class.java)
            orderIntent.putExtra("truckId", intent.getStringExtra("truckId"))
            orderIntent.putExtra("vendorId", this.intent.getStringExtra("vendorId"))
            orderIntent.putExtra("customerId", this.intent.getStringExtra("customerId"))
            startActivity(orderIntent)
        })
        var truckNameText = findViewById<TextView>(R.id.truckNameText)
        truckNameText.text = intent.getStringExtra("truckName")

        val addReviewButton = findViewById<Button>(R.id.addReviewButton)
        addReviewButton.setOnClickListener {
            addReviewWindow(intent.getStringExtra("truckName"), null)
        }
    }

    public override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        val truckId = intent.getStringExtra("truckId").toString()

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

                val foodListAdapter = FoodListAdapter(this@CustomerTruckInfoActivity, foodList)
                findViewById<ListView>(R.id.menuListView).adapter = foodListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })

        // review listener
        ref.child("reviews").orderByChild("truckId").equalTo(truckId)
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reviewList.clear()

                for (dataSnapshotChild in dataSnapshot.children) {
                    val review = reviewDao.constructReviewByHashMap(dataSnapshotChild)

                    reviewList.add(review)
                }

                val reviewListAdapter = ReviewListAdapter(this@CustomerTruckInfoActivity, reviewList)
                findViewById<ListView>(R.id.truckReviewListView).adapter = reviewListAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented")
            }
        })
    }
}
