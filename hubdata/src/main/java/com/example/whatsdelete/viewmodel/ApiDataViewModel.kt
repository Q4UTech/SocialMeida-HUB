package com.example.whatsdelete.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.whatsdelete.request.ApplicationListRequest
import com.example.whatsdelete.request.CategoryListRequest
import com.example.whatsdelete.request.CheckUpdateAPIRequest
import com.example.whatsdelete.responce.ApplicationListResponce


class ApiDataViewModel constructor(private val repository:Repository):ViewModel() {
    var categoryItemLiveData: MutableLiveData<ApplicationListResponce> = MutableLiveData<ApplicationListResponce>()


    fun callApiData(categoryRequestData: CategoryListRequest)=repository.getCategoryItem(categoryRequestData)
    fun callApplicationListData(applicationListRequest: ApplicationListRequest){
        println("WaTrandingStatus.callApplicationListView fasdgjhafsgh 009 bbb" + " ")
        categoryItemLiveData = repository.getApplicationListData(applicationListRequest)
    }
    fun callCheckUpdateAPI(checkUpdateAPI: CheckUpdateAPIRequest)=repository.getCheckUpdateAPI(checkUpdateAPI)

}