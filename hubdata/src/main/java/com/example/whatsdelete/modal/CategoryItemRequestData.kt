package com.example.whatsdelete.modal

import com.google.gson.annotations.SerializedName

data class CategoryItemRequestData(
    @SerializedName("country")
    var country:String? = null,
    @SerializedName("category_id")
    var category_id:String? = null,
    @SerializedName("app_id")
    var appId:String? = null
)
