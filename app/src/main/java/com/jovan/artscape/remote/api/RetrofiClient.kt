package com.jovan.artscape.remote.api

import com.jovan.artscape.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofiClient {
    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    private val okHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    fun getApiRegion(): ApiService {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(BuildConfig.ENDPOINT_REGION)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getApiArtSpace(): ApiService {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(BuildConfig.ENDPOINT_ARTSCAPE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getApiGenreClasification(): ApiService {
        val retrofit =
            Retrofit
                .Builder()
                .baseUrl(BuildConfig.ENDPOINT_GENRE_CLASSIFICATION)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create(ApiService::class.java)
    }
}
