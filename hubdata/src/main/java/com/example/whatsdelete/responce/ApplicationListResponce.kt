package com.example.whatsdelete.responce

data class ApplicationListResponce(
    val data: List<ApplicationListData>,
    val msz: String,
    val status: String
)