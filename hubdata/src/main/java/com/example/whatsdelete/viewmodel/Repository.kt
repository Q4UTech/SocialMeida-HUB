package com.example.whatsdelete.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.whatsdelete.api.ApiService
import com.example.whatsdelete.modal.*
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.request.CheckUpdateAPIRequest
import com.example.whatsdelete.responce.ApplicationListResponce
import com.example.whatsdelete.responce.CategoryListResponce
import com.example.whatsdelete.responce.CheckUpdateResponce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Repository constructor(private val retrofitService: ApiService) {
    val category= MutableLiveData<CategoryListResponce>()
    val categoryItem= MutableLiveData<ApplicationListResponce>()
    val checkUpdateLiveData= MutableLiveData<CheckUpdateResponce>()

    fun getCategoryItem(categoryRequestData: CategoryListRequest): LiveData<CategoryListResponce> {
        Log.d("TAG", "onViewCreated2: ")
        CoroutineScope(Dispatchers.IO).launch {
        val response=retrofitService.getCategory(categoryRequestData)

        response.enqueue(object : Callback<CategoryListResponce>{
            override fun onResponse(
                call: Call<CategoryListResponce>,
                response: Response<CategoryListResponce>
            ) {
                category.value=response.body()
                Log.d("TAG", "onResponse: "+response.body())
            }

            override fun onFailure(call: Call<CategoryListResponce>, t: Throwable) {
                Log.d("TAG", "onFailure: "+t.localizedMessage)
            }

        })}
        return category
    }

    fun getApplicationListData(applicationlistRequest: ApplicationListRequest): MutableLiveData<ApplicationListResponce> {
       CoroutineScope(Dispatchers.IO).launch {
           val response=retrofitService.getApplicationListAPI(applicationlistRequest)

           response.enqueue(object : Callback<ApplicationListResponce>{
               override fun onResponse(
                   call: Call<ApplicationListResponce>,
                   response: Response<ApplicationListResponce>
               ) {
                   categoryItem.value=response.body()
                   Log.d("TAG", "onResponse1: "+response.body())
               }

               override fun onFailure(call: Call<ApplicationListResponce>, t: Throwable) {
                   Log.d("TAG", "onFailure1: "+t.localizedMessage)
               }

           })
       }

        return categoryItem
    }

   fun getCheckUpdateAPI(checkUpdateAPI: CheckUpdateAPIRequest): LiveData<CheckUpdateResponce> {
       CoroutineScope(Dispatchers.IO).launch {
           val response=retrofitService.getCheckUpdateApi(checkUpdateAPI)

           response.enqueue(object : Callback<CheckUpdateResponce>{
               override fun onResponse(
                   call: Call<CheckUpdateResponce>,
                   response: Response<CheckUpdateResponce>
               ) {
                   checkUpdateLiveData.value=response.body()
                   Log.d("TAG", "onResponse1: "+response.body())
               }

               override fun onFailure(call: Call<CheckUpdateResponce>, t: Throwable) {
                   Log.d("TAG", "onFailure1: "+t.localizedMessage)
               }

           })
       }

        return checkUpdateLiveData
    }


}
