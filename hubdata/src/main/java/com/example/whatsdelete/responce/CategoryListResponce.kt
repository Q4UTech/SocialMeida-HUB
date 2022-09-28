package com.example.whatsdelete.responce

data class CategoryListResponce(
    val data : MutableList<CategoryListData>,
    val msz: String,
    val status: String
)