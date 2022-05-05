package com.example.myapplication

import android.app.Application
import android.util.Log
import com.example.myapplication.github.network.GithubProvider

class MyApplication : Application() {

    private val githubProvider = GithubProvider()
    val mainViewModelFactory = MyViewModelFactory(githubProvider)

    override fun onCreate() {
        super.onCreate()
        Log.d("MyAppliction", "started")
    }
}