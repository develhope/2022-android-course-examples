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

class GithubRepository(
    private val githubProvider: GithubProvider,
    private val repoDao: RepoDao
) {

    @Suppress("RedundantSuspendModifier")
    suspend fun loadRepos(): Flow<List<GithubRepo>> {
        return repoDao.getAll().map { list -> list.map { it.toModel() } }
            .also { Log.d("GithubRepository", "load data from DB") }
    }

    suspend fun retrieveRepos(username: String) {
        try {
            val dataFromNetwork = githubProvider.getUserRepos(username)
            repoDao.insertAll(*dataFromNetwork.map { repo -> repo.toEntity() }.toTypedArray())
        } catch (error: Exception) {
            Log.w("GithubRepository", "error loading data from network: $error")
        }
    }
}