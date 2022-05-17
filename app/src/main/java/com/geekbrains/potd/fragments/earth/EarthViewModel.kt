package com.geekbrains.potd.fragments.earth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.BuildConfig
import com.geekbrains.potd.nasa.EpicPhotosDTO
import com.geekbrains.potd.nasa.NasaRetrofitImpl
import com.geekbrains.potd.utils.NasaDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthViewModel : ViewModel() {
    sealed class RequestState {
        class Success(val response: EpicPhotosDTO, var currentPosition: Int) : RequestState()
        class Error(val error: String) : RequestState()
        class Loading : RequestState()
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
        retrofitImpl.nasaApi.getEpic(nasaDate.format(), BuildConfig.NASA_API_KEY).enqueue(callback)
        stateLiveData.value = RequestState.Loading()
    }

    fun setState(state: RequestState) {
        stateLiveData.postValue(state)
    }

    private val callback = object : Callback<EpicPhotosDTO> {
        override fun onResponse(call: Call<EpicPhotosDTO>, response: Response<EpicPhotosDTO>) {
            val body = response.body()
            if (response.isSuccessful && body != null) {
                stateLiveData.value = RequestState.Success(body, 0)
            } else {
                stateLiveData.value = RequestState.Error(response.message())
            }
        }

        override fun onFailure(call: Call<EpicPhotosDTO>, t: Throwable) {
            stateLiveData.value = RequestState.Error(t.toString())
        }
    }
}