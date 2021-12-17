package com.geekbrains.potd.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.BuildConfig
import com.geekbrains.potd.repository.POTDResponse
import com.geekbrains.potd.repository.POTDRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class POTDViewModel(
    private val stateLiveData: MutableLiveData<POTDState> = MutableLiveData(),
    private val retrofitImpl: POTDRetrofitImpl = POTDRetrofitImpl()
) : ViewModel() {
    fun getData(): LiveData<POTDState> {
        return stateLiveData
    }

    fun sendServerRequest() {
        stateLiveData.value = POTDState.Loading(0)
        val apiKey: String = BuildConfig.NASA_API_KEY
        retrofitImpl.get().getPOTD(apiKey).enqueue(callback)
    }

    val callback = object : Callback<POTDResponse> {
        override fun onResponse(call: Call<POTDResponse>, response: Response<POTDResponse>) {
            val body = response.body() // temp to satisfy kotlin null checker
            if (response.isSuccessful && body != null) {
                stateLiveData.value = POTDState.Success(body)
            } else {
                TODO("handle http error")
            }
        }

        override fun onFailure(call: Call<POTDResponse>, t: Throwable) {
            TODO("Not yet implemented")
        }
    }
}