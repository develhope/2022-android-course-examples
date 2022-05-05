package com.example.myapplication.github.network

import com.example.myapplication.github.network.dto.toGithubRepository
import com.example.myapplication.github.usecase.model.GithubRepo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PAGE_SIZE = 20

class GithubProvider {
    private val retrofit = Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(
        GsonConverterFactory.create()
    ).build()
    private val githubService = retrofit.create(GithubService::class.java)

    suspend fun getUserRepos(user: String): List<GithubRepo> =
        githubService.listRepos(user, PAGE_SIZE).map { it.toGithubRepository() }
}