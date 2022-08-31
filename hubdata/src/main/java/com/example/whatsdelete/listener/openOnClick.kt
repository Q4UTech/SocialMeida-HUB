package com.example.whatsdelete.listener

import android.view.View
import com.example.whatsdelete.modal.CategoryDetailItem

interface openOnClick {
    fun openItem(view: View, categoryDetailItem: CategoryDetailItem)
}