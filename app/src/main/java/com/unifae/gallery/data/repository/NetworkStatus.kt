package com.unifae.gallery.data.repository

class NetworkStatus(val status: Status, val message: String) {
    companion object {
        val LOADING_COMPLETE: NetworkStatus = NetworkStatus(Status.SUCCESS, "Success.")
        val LOADING: NetworkStatus = NetworkStatus(Status.ON_PROCESS, "On loading.")
        val ERROR: NetworkStatus = NetworkStatus(Status.FAILED, "Error.")
    }
}

enum class Status {
    SUCCESS,
    FAILED,
    ON_PROCESS,
}