package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface FootballApiService {
    @GET("countries")
    suspend fun listCountries(): CountryResult
}

class Country : Any()

class CountryResult : Any()

const val API_AUTHORIZATION_HEADER = "x-rapidapi-key"

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest = request.newBuilder().addHeader(
            API_AUTHORIZATION_HEADER,
            "faa0059974msha36b0fe1d21a91ep1bb15bjsn501894903502"
        ).build()

        return chain.proceed(newRequest)
    }

}

class MainActivity : AppCompatActivity() {
    val logging = HttpLoggingInterceptor()
    val authorization = AuthorizationInterceptor()
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authorization)
        .build()

    val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api-football-v1.p.rapidapi.com/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val footballApiService = retrofit.create(FootballApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logging.level = HttpLoggingInterceptor.Level.BODY
        retrieveCountries()
    }

    fun retrieveCountries() {
        val progress = findViewById<ContentLoadingProgressBar>(R.id.repo_loading_indicator)

        lifecycleScope.launch {
            try {
                progress.show()
                val countries = footballApiService.listCountries()
                progress.hide()
                showCountries(countries)
            } catch (e: Exception) {
                Log.e("MainActivity", "error retrieving countries: $e")
                progress.hide()
                Snackbar.make(
                    findViewById(R.id.main_view),
                    "Error retrieving countries",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") { retrieveCountries() }.show()
            }
        }
    }

    fun showCountries(countryResult: CountryResult) {
        Log.d("MainActivity", "list of countries received: $countryResult")
        Toast.makeText(this, "downloaded countries", Toast.LENGTH_LONG).show()
    }
}