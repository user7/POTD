package com.geekbrains.potd.view.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.BuildConfig
import com.geekbrains.potd.repository.POTDResponse
import com.geekbrains.potd.repository.POTDRetrofitImpl
import com.geekbrains.potd.viewmodel.POTDState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class POTDFragmentViewModel : ViewModel() {
    sealed class RequestState {
        data class Success(val response: POTDResponse) : RequestState()
        data class Loading(val progress: Int?) : RequestState()
        data class Error(val error: String) : RequestState()
    }

    private val stateLiveData: MutableLiveData<RequestState> = MutableLiveData()
    val state: LiveData<RequestState> = stateLiveData

    private val retrofitImpl: POTDRetrofitImpl = POTDRetrofitImpl()

    private var dayShift = 0
    fun adjustDayShift(value: Int, absolute: Boolean = false) {
        val newValue = if (absolute) value else dayShift + value
        if (newValue <= 0) {
            dayShift = newValue
            sendServerRequest()
        }
    }

    fun sendServerRequest() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, dayShift)
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone("EST")
        retrofitImpl.api.getPOTD(BuildConfig.NASA_API_KEY, fmt.format(cal.time)).enqueue(callback)
    }

    private val callback = object : Callback<POTDResponse> {
        override fun onResponse(call: Call<POTDResponse>, response: Response<POTDResponse>) {
            val body = response.body() // temp to satisfy kotlin null checker
            if (response.isSuccessful && body != null) {
                stateLiveData.value = RequestState.Success(body)
            } else {
                stateLiveData.value = RequestState.Error(response.message())
            }
        }

        override fun onFailure(call: Call<POTDResponse>, t: Throwable) {
            stateLiveData.value = RequestState.Error(t.toString())
        }
    }
}