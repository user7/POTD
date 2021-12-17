package com.geekbrains.potd.viewmodel

import com.geekbrains.potd.repository.POTDResponse

sealed class POTDState {
    data class Success(val potdResponse: POTDResponse): POTDState()
    data class Loading(val progress: Int?): POTDState()
    data class Error(val error: Throwable): POTDState()
}