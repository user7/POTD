package com.geekbrains.potd.view.api.mars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.BuildConfig
import com.geekbrains.potd.repository.MarsPhotosDTO
import com.geekbrains.potd.repository.NasaRetrofitImpl
import com.geekbrains.potd.view.api.utils.NasaDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsViewModel : ViewModel() {
    sealed class RequestState {
        data class Success(val response: MarsPhotosDTO) : RequestState()
        data class Loading(val progress: Int?) : RequestState()
        data class Error(val error: String) : RequestState()
    }

    private val stateLiveData: MutableLiveData<RequestState> = MutableLiveData()
    val state: LiveData<RequestState> = stateLiveData

    private val retrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl()

    val nasaDate = NasaDate()

    fun adjustDate(delta: Int) {
        if (nasaDate.adjust(delta))
            sendServerRequest()
    }

    fun resetDate() {
        if (nasaDate.reset())
            sendServerRequest()
    }

    fun sendServerRequest() {
        retrofitImpl.nasaApi.getMars(nasaDate.format(), BuildConfig.NASA_API_KEY).enqueue(callback)
    }

    private val callback = object : Callback<MarsPhotosDTO> {
        override fun onResponse(call: Call<MarsPhotosDTO>, response: Response<MarsPhotosDTO>) {
            val body = response.body() // temp to satisfy kotlin null checker
            if (response.isSuccessful && body != null) {
                stateLiveData.value = RequestState.Success(body)
            } else {
                stateLiveData.value = RequestState.Error(response.message())
            }
        }

        override fun onFailure(call: Call<MarsPhotosDTO>, t: Throwable) {
            stateLiveData.value = RequestState.Error(t.toString())
        }
    }
}