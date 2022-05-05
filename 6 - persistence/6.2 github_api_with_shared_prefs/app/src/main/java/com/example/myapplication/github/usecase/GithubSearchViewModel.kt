package com.example.myapplication.github.usecase

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.usecase.model.GithubRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

class GithubSearchViewModel(private val githubProvider: GithubProvider, private val preferences: SharedPreferences) : ViewModel() {

    private var _result = MutableLiveData<GithubSearchViewmodelEvent>()
    val result: LiveData<GithubSearchViewmodelEvent>
        get() = _result

    init {
        Log.d("MainVM", "init")
        checkFirstTimeUser(preferences)
    }

    private fun checkFirstTimeUser(preferences: SharedPreferences) {
        val firstTimeUser = preferences.getBoolean(KEY_FIRST_TIME_USER, true)
        Log.d("MainVM", "firstTimeUser: ${firstTimeUser}")

        if (firstTimeUser) {
            preferences.edit().putBoolean(KEY_FIRST_TIME_USER, false).apply()
            _result.value = GithubSearchViewmodelEvent.FirstTimeUser
        }
    }

    fun send(event: GithubSearchEvent) =
        when (event) {
            is GithubSearchEvent.RetrieveUserRepos -> retrieveRepos(event.name)
        }

    private fun retrieveRepos(username: String) {
        Log.d("MainViewModel", "retrieveRepos")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _result.value = GithubSearchViewmodelEvent.GithubSearchResult(githubProvider.getUserRepos(username))
            } catch (e: Exception) {
                _result.value =
                    GithubSearchViewmodelEvent.GithubSearchError("error retrieving user repos: ${e.localizedMessage}")
            }
        }
    }
}