package com.jatpack.socialmediahub.util

import android.view.View

interface SetClick {
    fun onClick(view: View, position: Int)
    fun onLongClcik(view: View, position: Int)

}