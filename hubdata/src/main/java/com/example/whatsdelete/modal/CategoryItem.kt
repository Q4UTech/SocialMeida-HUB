package com.example.whatsdelete.modal

import com.google.gson.annotations.SerializedName

data class CategoryItem(
    @SerializedName("data")
    var data: List<CategoryDetailItem>?=null,
    @SerializedName("msz")
    var msz: String?=null,
    @SerializedName("status")
    var status: String?=null
)

data class CategoryDetailItem(
    @SerializedName("cat_id")
    var cat_id: String?=null,
    @SerializedName("id")
    var id: String?=null,
    @SerializedName("image")
    var img: String?=null ,
    @SerializedName("count")
    var count: String?=null

)