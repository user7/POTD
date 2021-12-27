package com.geekbrains.potd.viewmodel

import com.geekbrains.potd.repository.PotdDTO

sealed class POTDState {
    data class Success(val potdResponse: PotdDTO): POTDState()
    data class Loading(val progress: Int?): POTDState()
    data class Error(val error: Throwable): POTDState()
}