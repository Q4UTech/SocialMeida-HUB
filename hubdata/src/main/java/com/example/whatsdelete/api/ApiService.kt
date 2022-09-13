package com.example.whatsdelete.api

import com.example.whatsdelete.modal.*
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.request.CheckUpdateAPIRequest
import com.example.whatsdelete.responce.ApplicationListResponce
import com.example.whatsdelete.responce.CategoryListResponce
import com.example.whatsdelete.responce.CheckUpdateResponce
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


    @POST("checkupdate/")
    fun getCheckUpdateApi(@Body checkUpdateAPIRequest: CheckUpdateAPIRequest):Call<CheckUpdateResponce>


}