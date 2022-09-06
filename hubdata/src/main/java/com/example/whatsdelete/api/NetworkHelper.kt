package com.example.whatsdelete.api

import android.content.Context
import com.example.whatsdelete.api.APIClient.apiService
import com.example.whatsdelete.constants.Constants
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NetworkHelper {




    fun getCrusherInInfoAPI(
        context: Context,
        networkListener: OnNetworkListener
    ) {

       // var requestCall = apiService?.getCategory();

       /* makeAPICall(
            context,
            requestCall,
            networkListener,
            ErrorResponse::class.java
        )*/
    }

    fun <T, E> makeAPICall(
        requestCall: Call<T>,
        networkListener: OnNetworkListener?,
        error: Class<E>
    ) {


        if (networkListener == null) {
            return
        }

        //  println("RetroApiClient ads calling 33333..."+requestCall.request().url.toString()+"  "+requestCall.request().headers.toString()+"  "+requestCall.request().body.toString())

        requestCall
            .enqueue(object : Callback<T> {
                override fun onResponse(
                    call: Call<T>,
                    response: Response<T>
                ) {
                    println("RetroApiClient ads calling 555..." + response.isSuccessful)

                    if (response.isSuccessful) {
                        networkListener.onSuccess(response.code(), response.body())
                    } else {

                        val errorResponse: E? = try {
                            Gson().fromJson<E>(
                                response.errorBody()?.string(),
                                error
                            )

                        } catch (e: Exception) {
                            e.printStackTrace()
                            null
                        }
                        println("RetroApiClient ads calling 6666...$errorResponse")

//                        if (response.code() == 500 || response.code() == 502) {
////                            Toast.makeText(context, "Internal Server Issue", Toast.LENGTH_LONG)
////                                .show()
//                        }
                        networkListener.onError(response.code(), errorResponse)
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    println("RetroApiClient ads calling 444..." + t.message + "  ")
                    networkListener.onError(Constants.RETROFIT_FAILURE, t.message)

                }
            })
    }

}