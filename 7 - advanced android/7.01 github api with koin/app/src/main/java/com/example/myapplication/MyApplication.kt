package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.github.AppDatabase
import com.example.myapplication.github.di.appModule
import com.example.myapplication.github.di.viewModels
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.repository.GithubRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("MyAppliction", "started")

        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(appModule, viewModels))
        }
    }
}