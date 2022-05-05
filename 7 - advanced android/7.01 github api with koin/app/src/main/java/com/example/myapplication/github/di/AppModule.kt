package com.example.myapplication.github.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.github.AppDatabase
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.repository.GithubRepository
import com.example.myapplication.github.usecase.GithubSearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single { GithubProvider() }
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "app-database"
        ).build()
    }
    single { androidContext().getSharedPreferences("app", Context.MODE_PRIVATE) }
    single {
        GithubRepository(
            githubProvider = get(),
            repoDao = (get() as AppDatabase).repoDao()
        )
    }
}

val viewModels = module {
    viewModel {
        GithubSearchViewModel(githubRepository = get(), preferences = get())
    }
}
