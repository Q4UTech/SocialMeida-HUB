package com.example.whatsdelete.modal

import com.google.gson.annotations.SerializedName

data class CategoryRequestData(
    @SerializedName("country")
    var country:String? = null,
    @SerializedName("app_id")
    var appId:String? = null
)