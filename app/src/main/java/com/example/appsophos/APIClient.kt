package com.example.appsophos

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: ApiService = retrofit.create(ApiService::class.java)
}