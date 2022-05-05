package com.example.myapplication

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.github.AppDatabase
import com.example.myapplication.github.usecase.GithubSearchViewModel
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.repository.GithubRepository

class MyViewModelFactory(
    private val githubRepository: GithubRepository,
    private val preferences: SharedPreferences,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubSearchViewModel::class.java)) {
            return GithubSearchViewModel(githubRepository, preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}