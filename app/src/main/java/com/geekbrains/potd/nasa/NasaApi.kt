package com.geekbrains.potd.nasa

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NasaApi {
    // Astronomy picture of the day
    @GET("planetary/apod")
    fun getPotd(
        @Query("api_key") apiKey: String,
        @Query("date") date: String,
    ) : Call<PotdDTO>

    // Earth Polychromatic Imaging Camera
    @GET("EPIC/api/natural/date/{date}")
    fun getEpic(
        @Path("date") date: String,
        @Query("api_key") apiKey: String,
    ): Call<EpicPhotosDTO>

    // Photos from Curiosity rover
    @GET("/mars-photos/api/v1/rovers/curiosity/photos")
    fun getMars(
        @Query("earth_date") date: String,
        @Query("api_key") apiKey: String,
    ): Call<MarsPhotosDTO>
}