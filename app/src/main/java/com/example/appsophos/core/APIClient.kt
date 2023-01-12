package com.example.appsophos.core

import com.example.appsophos.core.services.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {
    fun getRetrofit(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)

    }
}