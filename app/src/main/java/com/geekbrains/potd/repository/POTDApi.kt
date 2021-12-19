package com.geekbrains.potd.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface POTDApi {
    @GET("planetary/apod")
    fun getPOTD(
        @Query("api_key") apiKey: String,
        @Query("date") date: String,
    ) : Call<POTDResponse>
}