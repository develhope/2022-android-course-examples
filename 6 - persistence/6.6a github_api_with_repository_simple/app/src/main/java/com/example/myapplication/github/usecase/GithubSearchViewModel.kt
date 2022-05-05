package com.example.myapplication.github.usecase

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.github.repository.GithubRepository
import com.example.myapplication.github.repository.GithubRepositoryEvent
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
    data class GithubSearchError(val message: String?) : GithubSearchViewmodelState()
    object FirstTimeUser : GithubSearchViewmodelState()
}

const val KEY_FIRST_TIME_USER = "first_time_user"

class GithubSearchViewModel(
    private val githubRepository: GithubRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    val result = MutableSharedFlow<GithubSearchViewmodelState>()

    init {
        Log.d("GithubSearchViewModel", "init")
        checkFirstTimeUser(preferences)
        setupRepositoryObserver()
    }

    private fun checkFirstTimeUser(preferences: SharedPreferences) {
        val firstTimeUser = preferences.getBoolean(KEY_FIRST_TIME_USER, true)
        Log.d("GithubSearchViewModel", "firstTimeUser: ${firstTimeUser}")

        if (firstTimeUser) {
            preferences.edit().putBoolean(KEY_FIRST_TIME_USER, false).apply()

            CoroutineScope(Dispatchers.Main).launch {
                result.emit(GithubSearchViewmodelState.FirstTimeUser)
            }
        }
    }

    private fun setupRepositoryObserver() {
        viewModelScope.launch {
            githubRepository.loadRepos().collect {
                Log.d("GithubSearchViewModel", "received data: $it")
                result.emit(GithubSearchViewmodelState.GithubSearchResult(it))
            }
        }
    }

    fun send(event: GithubSearchEvent) =
        when (event) {
            is GithubSearchEvent.RetrieveUserRepos -> retrieveRepos(event.name)
        }

    private fun retrieveRepos(username: String) {
        Log.d("GithubSearchViewModel", "retrieveRepos")

        viewModelScope.launch { githubRepository.retrieveRepos(username) }
    }
}