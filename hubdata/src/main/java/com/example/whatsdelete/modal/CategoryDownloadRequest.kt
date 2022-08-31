package com.example.whatsdelete.modal

import com.google.gson.annotations.SerializedName

data class CategoryDownloadRequest(
    @SerializedName("imageid")
    var imageId:String? = null
)
