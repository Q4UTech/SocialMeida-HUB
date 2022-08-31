package com.example.whatsdelete.listener

import android.view.View
import com.example.whatsdelete.modal.Data
import com.example.whatsdelete.responce.CategoryListData

interface setClick {
    fun onClick(data: CategoryListData, position: String)
    fun onLongClcik(view: View, position: Int)

}