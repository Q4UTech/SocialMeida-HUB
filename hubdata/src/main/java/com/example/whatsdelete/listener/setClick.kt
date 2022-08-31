package com.example.whatsdelete.listener

import android.view.View
import com.example.whatsdelete.modal.Data

interface setClick {
    fun onClick(data: Data, position: Int,isServerHit: Boolean)
    fun onLongClcik(view: View, position: Int)

}