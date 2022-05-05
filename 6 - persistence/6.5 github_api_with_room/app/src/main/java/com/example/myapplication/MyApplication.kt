package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.github.AppDatabase
import com.example.myapplication.github.network.GithubProvider

class MyApplication : Application() {

    private val githubProvider = GithubProvider()
    lateinit var preferences: SharedPreferences
    lateinit var mainViewModelFactory: MyViewModelFactory
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        Log.d("MyAppliction", "started")
        preferences = getSharedPreferences("app", Context.MODE_PRIVATE)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        mainViewModelFactory = MyViewModelFactory(githubProvider, preferences, database)
    }
}