package com.osueat.hungry

import android.content.Intent
import android.os.Bundle
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
import com.osueat.hungry.data.model.CurrentCustomer
import com.osueat.hungry.model.*
import kotlinx.android.synthetic.main.activity_customer_truck_info.*
import kotlinx.android.synthetic.main.activity_customer_truck_info.view.*
import kotlinx.android.synthetic.main.activity_customer_truck_info.view.addReviewButton
import kotlinx.android.synthetic.main.activity_customer_truck_info.view.createOrderButton
import kotlinx.android.synthetic.main.activity_customer_truck_info.view.createOrderButton
import kotlinx.android.synthetic.main.layout_add_review.view.*
import java.util.*
import kotlin.collections.ArrayList


class CustomerTruckInfoActivity : AppCompatActivity() {

    private val TAG = "TruckInfoActivity"

    private val foodList = ArrayList<Food>()
    private val reviewList = ArrayList<Review>()
    private val ref = FirebaseDatabase.getInstance().reference
    private val foodDao = FoodDao(ref)
    private val reviewDao = ReviewDao(ref)


    private fun addReviewWindow(truckName: String) {
        // add reviews
        val addReviewAlertDialog = AlertDialog.Builder(this@CustomerTruckInfoActivity)
        val addReviewView = layoutInflater.inflate(R.layout.layout_add_review, null)
        addReviewAlertDialog.setView(addReviewView)
        addReviewAlertDialog.setTitle(truckName)

        // show add review window
        val alertWindow = addReviewAlertDialog.create()
        alertWindow.show()

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
        }

        addReviewView.cancelReviewButton.setOnClickListener(View.OnClickListener {
            alertWindow.dismiss()
        })
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
            addReviewWindow(intent.getStringExtra("truckName"))
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
