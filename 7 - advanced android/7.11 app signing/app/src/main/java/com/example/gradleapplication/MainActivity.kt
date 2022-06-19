package com.example.gradleapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.text)

        val appName = resources.getString(R.string.app_name)

        if (BuildConfig.DEBUG) {
            //
            textview.text = "Debug"
        }
    }
}