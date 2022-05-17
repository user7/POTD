package com.geekbrains.potd.fragments.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.BuildConfig
import com.geekbrains.potd.nasa.PotdDTO
import com.geekbrains.potd.nasa.NasaRetrofitImpl
import com.geekbrains.potd.utils.NasaDate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SystemViewModel : ViewModel() {
    sealed class RequestState {
        data class Success(val response: PotdDTO) : RequestState()
        data class Loading(val progress: Int?) : RequestState()
        data class Error(val error: String) : RequestState()
    }

    private val stateLiveData: MutableLiveData<RequestState> = MutableLiveData()
    val state: LiveData<RequestState> = stateLiveData

    private val retrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl()

    val nasaDate = NasaDate()
    fun adjustDayShift(value: Int) {
        if (nasaDate.adjust(value) || value == 0)
            sendServerRequest()
    }

    fun resetDayShift() {
        if (nasaDate.reset())
            sendServerRequest()
    }

    fun sendServerRequest() {
        retrofitImpl.nasaApi.getPotd(BuildConfig.NASA_API_KEY, nasaDate.format()).enqueue(callback)
    }

    private val callback = object : Callback<PotdDTO> {
        override fun onResponse(call: Call<PotdDTO>, response: Response<PotdDTO>) {
            val body = response.body() // temp to satisfy kotlin null checker
            if (response.isSuccessful && body != null) {
                stateLiveData.value = RequestState.Success(body)
            } else {
                stateLiveData.value = RequestState.Error(response.message())
            }
        }

        override fun onFailure(call: Call<PotdDTO>, t: Throwable) {
            stateLiveData.value = RequestState.Error(t.toString())
        }
    }
}