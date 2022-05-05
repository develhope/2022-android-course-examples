package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.github.AppDatabase
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.repository.GithubRepository

class MyApplication : Application() {

    lateinit var database: AppDatabase
    private val githubProvider = GithubProvider()
    lateinit var githubRepository: GithubRepository
    lateinit var mainViewModelFactory: MyViewModelFactory
    lateinit var preferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        Log.d("MyAppliction", "started")
        preferences = getSharedPreferences("app", Context.MODE_PRIVATE)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()
        githubRepository = GithubRepository(githubProvider, database.repoDao())

        mainViewModelFactory = MyViewModelFactory(githubRepository, preferences)
    }
}