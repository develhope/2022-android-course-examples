package com.example.myapplication.github.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.github.usecase.GithubSearchViewModel
import com.example.myapplication.github.usecase.GithubSearchEvent
import com.example.myapplication.github.usecase.GithubSearchViewmodelState
import com.example.myapplication.github.usecase.model.GithubRepo
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GithubSearchScreen : AppCompatActivity() {

    private lateinit var viewModel: GithubSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel =
            (application as MyApplication).mainViewModelFactory.create(GithubSearchViewModel::class.java)

        observeRepos()
        viewModel.send(GithubSearchEvent.RetrieveUserRepos("mrsasha"))

    }

    fun observeRepos() {
        val progress = findViewById<ContentLoadingProgressBar>(R.id.repo_loading_indicator)
        progress.show()

        lifecycleScope.launch {
            viewModel.result.collect {
                progress.hide()
                Log.d("MainActivity", "event: $it")

                when (it) {
                    is GithubSearchViewmodelState.GithubSearchResult -> showRepos(it.repos)
                    is GithubSearchViewmodelState.GithubSearchError -> Snackbar.make(
                        findViewById(R.id.main_view),
                        "Error retrieving repos: $it",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Retry") { viewModel.send(GithubSearchEvent.RetrieveUserRepos("mrsasha")) }
                        .show()
                    is GithubSearchViewmodelState.FirstTimeUser -> Toast.makeText(
                        this@GithubSearchScreen,
                        "Hi, nice to have you here!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    fun showRepos(repoResults: List<GithubRepo>) {
        Log.d("MainActivity", "list of repos received, size: ${repoResults.size}")

        val list = findViewById<RecyclerView>(R.id.repo_list)
        list.visibility = View.VISIBLE
        val adapter = RepoAdapter(repoResults)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)
    }
}