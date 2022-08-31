package com.example.whatsdelete.viewmodel

import androidx.lifecycle.ViewModel
import com.example.whatsdelete.modal.CategoryDownloadRequest
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest


class ApiDataViewModel constructor(private val repository:WhatsDeleteRepository):ViewModel() {


    fun callApiData(categoryRequestData: CategoryListRequest)=repository.getCategoryItem(categoryRequestData)
    fun callApplicationListData(applicationListRequest: ApplicationListRequest)=repository.getApplicationListData(applicationListRequest)

}