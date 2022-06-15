package com.example.dateformatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dateView = findViewById<TextView>(R.id.date)
        val date = Date()
        val formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        dateView.text = formatter.format(date)
    }
}