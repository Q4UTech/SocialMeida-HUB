package com.example.whatsdelete.request

data class ApplicationListRequest(
    val app_id: String,
    val category_id: String,
    val country: String
)