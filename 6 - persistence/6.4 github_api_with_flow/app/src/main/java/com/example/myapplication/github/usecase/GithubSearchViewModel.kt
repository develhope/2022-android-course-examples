package com.example.myapplication.github.usecase

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.usecase.model.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

sealed class GithubSearchEvent {
    data class RetrieveUserRepos(val name: String) : GithubSearchEvent()
}

sealed class GithubSearchViewmodelEvent {
    data class GithubSearchResult(val repos: List<GithubRepository>) : GithubSearchViewmodelEvent()
    data class GithubSearchError(val message: String) : GithubSearchViewmodelEvent()
    object FirstTimeUser : GithubSearchViewmodelEvent()
}

const val KEY_FIRST_TIME_USER = "first_time_user"

class GithubSearchViewModel(
    private val githubProvider: GithubProvider,
    private val preferences: SharedPreferences
) : ViewModel() {

    val result = MutableSharedFlow<GithubSearchViewmodelEvent>()

    init {
        Log.d("MainVM", "init")
        checkFirstTimeUser(preferences)
    }

    private fun checkFirstTimeUser(preferences: SharedPreferences) {
        val firstTimeUser = preferences.getBoolean(KEY_FIRST_TIME_USER, true)
        Log.d("MainVM", "firstTimeUser: ${firstTimeUser}")

        if (firstTimeUser) {
            preferences.edit().putBoolean(KEY_FIRST_TIME_USER, false).apply()

            CoroutineScope(Dispatchers.Main).launch {
                result.emit(GithubSearchViewmodelEvent.FirstTimeUser)
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
                result.emit(
                    GithubSearchViewmodelEvent.GithubSearchResult(
                        githubProvider.getUserRepos(
                            username
                        )
                    )
                )
            } catch (e: Exception) {
                result.emit(
                    GithubSearchViewmodelEvent.GithubSearchError("error retrieving user repos: ${e.localizedMessage}")
                )
            }
        }
    }
}