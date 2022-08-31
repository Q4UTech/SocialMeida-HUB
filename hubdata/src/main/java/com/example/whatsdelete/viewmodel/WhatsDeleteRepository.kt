package com.example.whatsdelete.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.whatsdelete.api.ApiService
import com.example.whatsdelete.constants.Constants
import com.example.whatsdelete.modal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WhatsDeleteRepository constructor(private val retrofitService: ApiService) {
    val category= MutableLiveData<Category>()
    val categoryItem= MutableLiveData<CategoryItem>()

    fun getCategoryItem(categoryRequestData: CategoryRequestData): LiveData<Category> {
        Log.d("TAG", "onViewCreated2: ")
        CoroutineScope(Dispatchers.IO).launch {
        val response=retrofitService.getCategory(categoryRequestData)

        response.enqueue(object : Callback<Category>{
            override fun onResponse(
                call: Call<Category>,
                response: Response<Category>
            ) {
                category.value=response.body()
                Log.d("TAG", "onResponse: "+response.body())
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                Log.d("TAG", "onFailure: "+t.localizedMessage)
            }

        })}
        return category
    }

    fun getCategoryItemData(categoryItemRequestData: CategoryItemRequestData): LiveData<CategoryItem> {
       CoroutineScope(Dispatchers.IO).launch {
           val response=retrofitService.getCategoryItems(categoryItemRequestData)

           response.enqueue(object : Callback<CategoryItem>{
               override fun onResponse(
                   call: Call<CategoryItem>,
                   response: Response<CategoryItem>
               ) {
                   categoryItem.value=response.body()
                   Log.d("TAG", "onResponse1: "+response.body())
               }

               override fun onFailure(call: Call<CategoryItem>, t: Throwable) {
                   Log.d("TAG", "onFailure1: "+t.localizedMessage)
               }

           })
       }

        return categoryItem
    }


    fun getCategoryDownloadCountAPI(categoryDownloadRequest: CategoryDownloadRequest): LiveData<CategoryItem> {
       CoroutineScope(Dispatchers.IO).launch {
           val response=retrofitService.getCategoryDownloadCount(categoryDownloadRequest)

//           response.enqueue(object : Callback<CategoryItem>{
//               override fun onResponse(
//                   call: Call<CategoryItem>,
//                   response: Response<CategoryItem>
//               ) {
////                   categoryItem.value=response.body()
////                   Log.d("TAG", "onResponse1: "+response.body())
//               }
//
//               override fun onFailure(call: Call<CategoryItem>, t: Throwable) {
//                   Log.d("TAG", "onFailure1: "+t.localizedMessage)
//               }
//
//           })
       }

        return categoryItem
    }
}
