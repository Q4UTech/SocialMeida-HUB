package com.example.whatsdelete.api

import com.example.whatsdelete.modal.*
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.responce.ApplicationListResponce
import com.example.whatsdelete.responce.CategoryListData
import com.example.whatsdelete.responce.CategoryListResponce
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

   /* @POST("posts")
    fun getServerData(): Call<List<FakeDataItem>>*/


    @POST("categorylist/100/0")
    fun getCategory(
       @Body categoryRequestData: CategoryListRequest
    ):Call<CategoryListResponce>


    @POST("categoryimglist/100/0")
    fun getApplicationListAPI(
       @Body applicationListRequest: ApplicationListRequest
    ):Call<ApplicationListResponce>


    @POST("downloadimg/")
    fun getCategoryDownloadCount(@Body categoryDownloadRequest: CategoryDownloadRequest):Call<CategoryItem>


}