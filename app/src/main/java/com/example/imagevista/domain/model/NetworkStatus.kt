package com.example.imagevista.domain.model

sealed class NetworkStatus {

    data object Connected : NetworkStatus()

    data object DisConnected : NetworkStatus()
}