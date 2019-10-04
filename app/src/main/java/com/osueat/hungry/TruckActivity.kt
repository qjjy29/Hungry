package com.osueat.hungry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class TruckActivity : AppCompatActivity() {

    private val TAG = "TruckActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck)
        Log.d(TAG, "onCreate() called")
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
