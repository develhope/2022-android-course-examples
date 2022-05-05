package com.example.myapplication.github.network

import com.example.myapplication.github.network.dto.RepoResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{user}/repos")
    suspend fun listRepos(@Path("user") user: String, @Query("per_page") pageSize: Int): RepoResult
}