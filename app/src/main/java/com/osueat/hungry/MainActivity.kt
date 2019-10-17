package com.osueat.hungry

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.FirebaseDatabase
import com.osueat.hungry.model.User
import java.util.*
import com.osueat.hungry.model.UserDao

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        var button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            var intent = Intent(this, TruckActivity::class.java)
            startActivity(intent)
        }

        Log.d(TAG, "onCreate() called")

        // test code added by Yun to show how to use functions in xxxDao, should be removed later
        val ref = FirebaseDatabase.getInstance().reference

        val id = UUID.randomUUID().toString()
        val username = "aalok"
        val password = "aalok"
        val createDate = Date().time
        val lastUpdateDate = Date().time
        val lastLoginDate = Date().time
        val type = "CUSTOMER"
        val phoneNumber = "1234567802"
        val email = "aalok@gmail.com"

        val newUser = User(id, username, password, createDate, lastUpdateDate,
            lastLoginDate, phoneNumber, email, type)

        val userDao = UserDao(ref)
        userDao.createUser(newUser)

        val updatedUser = User(id, username, password, createDate, lastUpdateDate,
            lastLoginDate, "9999999999", email, type)
        userDao.updateUserById(id, updatedUser)
        userDao.deleteUserById(id)
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
