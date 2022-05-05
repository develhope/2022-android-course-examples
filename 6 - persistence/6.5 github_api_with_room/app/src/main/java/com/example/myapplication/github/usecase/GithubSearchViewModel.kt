package com.example.myapplication.github.usecase

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.repository.dao.RepoDao
import com.example.myapplication.github.repository.entity.toEntity
import com.example.myapplication.github.repository.entity.toModel
import com.example.myapplication.github.usecase.model.GithubRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class GithubSearchEvent {
    data class RetrieveUserRepos(val name: String) : GithubSearchEvent()
}

sealed class GithubSearchViewmodelState {
    data class GithubSearchResult(val repos: List<GithubRepo>) : GithubSearchViewmodelState()
    data class GithubSearchError(val message: String) : GithubSearchViewmodelState()
    object FirstTimeUser : GithubSearchViewmodelState()
}

const val KEY_FIRST_TIME_USER = "first_time_user"

class GithubSearchViewModel(
    private val githubProvider: GithubProvider,
    private val preferences: SharedPreferences,
    private val repoDao: RepoDao
) : ViewModel() {

    val result = MutableSharedFlow<GithubSearchViewmodelState>()

    init {
        Log.d("MainVM", "init")
        checkFirstTimeUser(preferences)
        setupDatabaseObserver()
    }

    private fun checkFirstTimeUser(preferences: SharedPreferences) {
        val firstTimeUser = preferences.getBoolean(KEY_FIRST_TIME_USER, true)
        Log.d("MainVM", "firstTimeUser: ${firstTimeUser}")

        if (firstTimeUser) {
            preferences.edit().putBoolean(KEY_FIRST_TIME_USER, false).apply()

            CoroutineScope(Dispatchers.Main).launch {
                result.emit(GithubSearchViewmodelState.FirstTimeUser)
            }
        }
    }

    private fun setupDatabaseObserver() {
        viewModelScope.launch {
            repoDao.getAll().collect {
                Log.d("MainViewModel", "retrieved from database")
                result.emit(GithubSearchViewmodelState.GithubSearchResult(
                    it.map { entity -> entity.toModel() }
                ))
            }
        }
    }

    fun send(event: GithubSearchEvent) =
        when (event) {
            is GithubSearchEvent.RetrieveUserRepos -> retrieveRepos(event.name)
        }

    private fun retrieveRepos(username: String) {
        Log.d("MainViewModel", "retrieveRepos")

        viewModelScope.launch {
            try {
                Log.d("MainViewModel", "retrieved from network")
                val dataFromNetwork = githubProvider.getUserRepos(
                    username
                )
                repoDao.insertAll(*dataFromNetwork.map { repo -> repo.toEntity() }.toTypedArray())
            } catch (e: Exception) {
                result.emit(
                    GithubSearchViewmodelState.GithubSearchError("error retrieving user repos: ${e.localizedMessage}")
                )
            }
        }
    }
}