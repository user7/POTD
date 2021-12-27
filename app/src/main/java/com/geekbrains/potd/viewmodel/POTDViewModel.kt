package com.geekbrains.potd.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekbrains.potd.BuildConfig
import com.geekbrains.potd.repository.PotdDTO
import com.geekbrains.potd.repository.NasaRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class POTDViewModel(
    private val stateLiveData: MutableLiveData<POTDState> = MutableLiveData(),
    private val retrofitImpl: NasaRetrofitImpl = NasaRetrofitImpl(),
) : ViewModel() {
    fun getData(): LiveData<POTDState> {
        return stateLiveData
    }

    private var dayShift = 0
    fun setDayShift(dayShift_: Int) {
        dayShift = dayShift_
        sendServerRequest()
    }

    fun sendServerRequest() {
        stateLiveData.value = POTDState.Loading(0)
        val apiKey: String = BuildConfig.NASA_API_KEY
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, dayShift)
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone("EST")
        retrofitImpl.nasaApi.getPotd(apiKey, fmt.format(cal.time)).enqueue(callback)
    }

    val callback = object : Callback<PotdDTO> {
        override fun onResponse(call: Call<PotdDTO>, response: Response<PotdDTO>) {
            val body = response.body() // temp to satisfy kotlin null checker
            if (response.isSuccessful && body != null) {
                stateLiveData.value = POTDState.Success(body)
            } else {
                Log.d("==", "bad response ${response}")
            }
        }

        override fun onFailure(call: Call<PotdDTO>, t: Throwable) {
            Log.d("==", "request failed $t")
        }
    }

    var themeId: Int? = null
}