package com.example.whatsdelete.api

import android.util.Log
import com.example.whatsdelete.constants.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object APIClient {
    var apiService: ApiService? = null

    fun getNetworkService(): ApiService {

        if (apiService == null) {
            synchronized(APIClient::class.java) {
                if (apiService == null) {
                    apiService = setUpRetrofitApiService()
                }
            }
        }

        return apiService!!
    }

    private fun setUpRetrofitApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URl)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }


    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        // Add logging for API services
       /* if (BuildConfig.DEBUG) {
            addLogging(builder)
        }*/
       addLogging(builder)
        // Add read, write, connect timeout
       //// addTimeout(builder)
      //  addHeader(builder)
        return builder.build()
    }

    private fun addHeader(builder: OkHttpClient.Builder) {
        builder.interceptors().add(Interceptor { chain ->
            Log.d("TAG", "addHeader: "+chain.toString())
            /* val authToken: String? = ""
             val userId: String? = ""
             if (authToken == null || userId == null) {
                 return@Interceptor chain.proceed(chain.request())
             }*/

            val request = chain.request()
                .newBuilder()
                //.addHeader("Authorization", authToken)
                .build()

            return@Interceptor chain.proceed(request)

        })
    }

    private fun addTimeout(builder: OkHttpClient.Builder) {
        builder.connectTimeout(Constants.CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
    }

    private fun addLogging(builder: OkHttpClient.Builder) {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = (HttpLoggingInterceptor.Level.BODY)
        builder.networkInterceptors().add(httpLoggingInterceptor)
    }
}