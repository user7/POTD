package com.geekbrains.potd.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class POTDRetrofitImpl {
    private val baseUrl = "https://api.nasa.gov/"
    val api: POTDApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        retrofit.create(POTDApi::class.java)
    }
}