package com.example.whatsdelete.api

interface OnNetworkListener {
    fun onSuccess(httpCode: Int, response: Any?)
    fun onError(httpCode: Int, error: Any?)
}