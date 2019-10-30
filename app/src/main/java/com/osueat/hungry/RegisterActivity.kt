package com.osueat.hungry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import android.R.attr.button
import android.view.View
import android.widget.CheckBox
import com.osueat.hungry.model.*


class RegisterActivity : AppCompatActivity() {

    private fun checkValid(username: String, password: String, confirmPassword: String,
                           email: String, phoneNumber: String): Boolean {
        if (password != confirmPassword) {
            return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val usernameField = findViewById<EditText>(R.id.register_username)
        val passwordField = findViewById<EditText>(R.id.register_password)
        val confirmPasswordField = findViewById<EditText>(R.id.register_confirm)
        val emailField = findViewById<EditText>(R.id.register_email)
        val phoneNumberField = findViewById<EditText>(R.id.register_phone_number)

        val registerDone = findViewById<Button>(R.id.register_done)

        val vendorCheckBox = findViewById<CheckBox>(R.id.register_vendor)

        registerDone.isEnabled = true


        registerDone.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()
            val email = emailField.text.toString()
            val phoneNumber = phoneNumberField.text.toString()

            if (checkValid(username, password, confirmPassword, email, phoneNumber)) {
                val ref = FirebaseDatabase.getInstance().reference
                val userDao = UserDao(ref)
                val id = UUID.randomUUID().toString()
                val createDate = Date()
                val lastUpdateDate = Date()
                val lastLoginDate = Date()
                val type = if (vendorCheckBox.isChecked) "VENDOR" else "CUSTOMER"

                val newUser = User(id, username, password, createDate,
                    lastUpdateDate, lastLoginDate, phoneNumber, email, type)
                userDao.createUser(newUser)
                if (vendorCheckBox.isChecked) {
                    val vendorId = UUID.randomUUID().toString()
                    val nickname = "Great vendor"
                    val balance = 5.0
                    val truckIdList = emptyList<String>()
                    val vendor = Vendor(vendorId, id, nickname, truckIdList, balance)
                    val vendorDao = VendorDao(ref)
                    vendorDao.createVendor(vendor)
                } else {
                    val customerId = UUID.randomUUID().toString()
                    val nickname = "Great customer"
                    val balance = 0.0
                    val customer = Customer(customerId, id, nickname, balance)
                    val customerDao = CustomerDao(ref)
                    customerDao.createCustomer(customer)
                }
                finish()
            }

        }
    }
}
