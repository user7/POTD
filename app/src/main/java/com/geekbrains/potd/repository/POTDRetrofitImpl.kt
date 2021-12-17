package com.geekbrains.potd.repository

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class POTDRetrofitImpl {
    private val baseUrl = "https://api.nasa.gov/"
    fun get(): POTDApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return retrofit.create(POTDApi::class.java)
    }
}