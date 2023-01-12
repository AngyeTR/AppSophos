package com.example.appsophos.di

import android.net.*
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.appsophos.core.services.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @RequiresApi(Build.VERSION_CODES.M)
    @Singleton
    @Provides
    fun provideRetrofit(): ApiService {
        return Retrofit.Builder()
                .baseUrl("https://6w33tkx4f9.execute-api.us-east-1.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)}
}






