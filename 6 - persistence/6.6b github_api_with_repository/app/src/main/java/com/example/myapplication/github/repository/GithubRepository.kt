package com.example.myapplication.github.repository

import android.util.Log
import com.example.myapplication.github.network.GithubProvider
import com.example.myapplication.github.repository.dao.RepoDao
import com.example.myapplication.github.repository.entity.toEntity
import com.example.myapplication.github.repository.entity.toModel
import com.example.myapplication.github.usecase.model.GithubRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

sealed class GithubResult() {
    data class ReposLoaded(val repos: List<GithubRepo>) : GithubResult()
    data class ReposLoadingError(val message: String?) : GithubResult()
}

class GithubRepository(
    private val githubProvider: GithubProvider,
    private val repoDao: RepoDao
) {

    private val result = MutableSharedFlow<GithubResult>()

    @Suppress("RedundantSuspendModifier")
    suspend fun loadRepos(): Flow<GithubResult> {
        repoDao.getAll().map { list -> list.map { it.toModel() } }.collect {
            Log.d("GithubRepository", "retrieveRepos from database: $it")
            result.emit(GithubResult.ReposLoaded(it))
        }

        return result
    }

    suspend fun retrieveRepos(username: String) {
        Log.d("GithubRepository", "retrieveRepos")
        try {
            val dataFromNetwork = githubProvider.getUserRepos(username)
            Log.d("GithubRepository", "retrieveRepos from network: $dataFromNetwork")
            repoDao.insertAll(*dataFromNetwork.map { repo -> repo.toEntity() }.toTypedArray())
        } catch (error: Exception) {
            Log.w("GithubRepository", "error loading data from network: $error")
            result.emit(GithubResult.ReposLoadingError(error.message))
        }
    }
}