package com.example.whatsdelete.modal

import com.example.whatsdelete.responce.ApplicationListData

data class ApplicationModelDataList(
    val data: ArrayList<ApplicationListData>,
    val cat_id: String
)