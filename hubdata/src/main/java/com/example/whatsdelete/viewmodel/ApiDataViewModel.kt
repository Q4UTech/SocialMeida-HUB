package com.example.whatsdelete.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsdelete.modal.CategoryDownloadRequest
import com.example.whatsdelete.modal.CategoryItemRequestData
import com.example.whatsdelete.modal.CategoryRequestData
import com.example.whatsdelete.modal.FakeDataItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class ApiDataViewModel constructor(private val repository:WhatsDeleteRepository):ViewModel() {


    fun callApiData(categoryRequestData: CategoryRequestData)=repository.getCategoryItem(categoryRequestData)
    fun callCategoryItemData(categoryItemRequestData: CategoryItemRequestData)=repository.getCategoryItemData(categoryItemRequestData)
    fun callCategoryDownloadCount(categoryDownloadRequest: CategoryDownloadRequest)=repository.getCategoryDownloadCountAPI(categoryDownloadRequest)
}