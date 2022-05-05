package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.github.network.GithubProvider

class MyApplication : Application() {

    private val githubProvider = GithubProvider()
    lateinit var preferences: SharedPreferences
    lateinit var mainViewModelFactory: MyViewModelFactory

    override fun onCreate() {
        super.onCreate()
        Log.d("MyAppliction", "started")
        preferences = getSharedPreferences("app", Context.MODE_PRIVATE)
        mainViewModelFactory = MyViewModelFactory(githubProvider, preferences)
    }
}