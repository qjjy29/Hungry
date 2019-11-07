package com.osueat.hungry.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.osueat.hungry.MainActivity

import com.osueat.hungry.R
import com.osueat.hungry.RegisterActivity
import com.osueat.hungry.data.model.CurrentCustomer
import com.osueat.hungry.data.model.CurrentUser
import com.osueat.hungry.model.Customer
import com.osueat.hungry.model.User
import com.osueat.hungry.model.UserDao
import com.osueat.hungry.ui.vendor.VendorMainActivity
import java.util.*
import kotlin.collections.HashMap

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private val TAG = "LoginActivity"

    private lateinit var loading: ProgressBar
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var register: Button

    private fun updateCurrentUser(user: User) {
        CurrentUser.setCurrentUser(user)
    }

    private fun auth(username: String, password: String, it: Context) {
        val ref = FirebaseDatabase.getInstance().reference
        val userDao = UserDao(ref)
        Log.d(TAG, "auth")
        ref.child("users").orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (dataSnapshotChild in dataSnapshot.children) {
                    val currentUser = userDao.constructUserByHashMap(dataSnapshotChild)
                    if (currentUser.password == password) {
                        // update the lastLoginDate
                        val userDao = UserDao(ref)
                        val updatedUser = User(currentUser.id, currentUser.username, currentUser.password,
                            currentUser.createDate, Date(), currentUser.lastUpdateDate,
                            currentUser.phoneNumber, currentUser.email, currentUser.type)
                        userDao.updateUserById(currentUser.id, updatedUser)
                        updateCurrentUser(currentUser)
                        // check if user is customer
                        if (currentUser.type == "CUSTOMER") {
                            val intent = Intent(it, MainActivity::class.java)
                            intent.putExtra("customerId", currentUser.id)
                            startActivity(intent)
                        }
                        // else user is vendor
                        else {
                            val intent = Intent(it, VendorMainActivity::class.java)
                            intent.putExtra("vendorId", currentUser.id)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(applicationContext, "Invalid username/password, please try again",
                            Toast.LENGTH_LONG).show()
                    }
//                    val intent = Intent(it, MainActivity::class.java)
//                    startActivity(intent)
                    break
                }
                loading.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")

        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login)
        loading = findViewById(R.id.loading)
        register = findViewById(R.id.register)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            // go to the main activity

            // if vendor, go to vendor home page
            var vendorCheckBox: CheckBox = findViewById(R.id.vendorCheckBox)
            if (vendorCheckBox.isChecked) {
                val intent = Intent(this, VendorMainActivity::class.java)
                startActivity(intent)
            }

            else {
                // else go to customer main activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            auth(username.text.toString(), password.text.toString(), this)

        }

        register.isEnabled = true
        register.setOnClickListener {
            // else go to register activity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = "Welcome, great customer"
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
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

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
