package com.example.myapplication4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.example.myapplication4.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.actionButton.setOnClickListener {
            binding.first.text = binding.second.text
        }

        binding.second.doOnTextChanged { text, start, before, count ->
            text?.let {
                binding.actionButton.isEnabled = it.length >= 5 && it.contains("@")
            }
        }
    }
}