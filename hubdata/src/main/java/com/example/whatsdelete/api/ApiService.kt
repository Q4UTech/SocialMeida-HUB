package com.example.whatsdelete.api

import com.example.whatsdelete.modal.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

   /* @POST("posts")
    fun getServerData(): Call<List<FakeDataItem>>*/


    @POST("categorylist/50/0")
    fun getCategory(
       @Body categoryRequestData: CategoryRequestData
    ):Call<Category>


    @POST("categoryimglist/100/0")
    fun getCategoryItems(
       @Body categoryItemRequestData: CategoryItemRequestData
    ):Call<CategoryItem>


    @POST("downloadimg/")
    fun getCategoryDownloadCount(@Body categoryDownloadRequest: CategoryDownloadRequest):Call<CategoryItem>


}