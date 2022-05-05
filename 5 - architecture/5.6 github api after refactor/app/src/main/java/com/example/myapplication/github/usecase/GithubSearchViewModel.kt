package com.example.myapplication.github.usecase

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

sealed class GithubSearchResult {
    data class Result(val repos: List<GithubRepository>) : GithubSearchResult()
    data class Error(val message: String) : GithubSearchResult()
}

class GithubSearchViewModel(private val githubProvider: GithubProvider) : ViewModel() {

    private var _result = MutableLiveData<GithubSearchResult>()
    val result: LiveData<GithubSearchResult>
        get() = _result


    fun send(event: GithubSearchEvent) =
        when (event) {
            is GithubSearchEvent.RetrieveUserRepos -> retrieveRepos(event.name)
        }

    private fun retrieveRepos(username: String) {
        Log.d("MainViewModel", "retrieveRepos")
        CoroutineScope(Dispatchers.Main).launch {
            try {
                _result.value = GithubSearchResult.Result(githubProvider.getUserRepos(username))
            } catch (e: Exception) {
                _result.value =
                    GithubSearchResult.Error("error retrieving user repos: ${e.localizedMessage}")
            }
        }
    }
}