package com.example.myapplication

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.github.AppDatabase
import com.example.myapplication.github.usecase.GithubSearchViewModel
import com.example.myapplication.github.network.GithubProvider

class MyViewModelFactory(
    private val githubProvider: GithubProvider,
    private val preferences: SharedPreferences,
    private val database: AppDatabase,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubSearchViewModel::class.java)) {
            return GithubSearchViewModel(githubProvider, preferences, database.repoDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}