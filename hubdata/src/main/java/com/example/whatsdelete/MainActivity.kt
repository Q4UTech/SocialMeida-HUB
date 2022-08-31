package com.example.whatsdelete

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whatsdelete.fragments.WaTrandingStatus
import com.jatpack.wastatustranding.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment=WaTrandingStatus()
        supportFragmentManager.beginTransaction().add(R.id.container,homeFragment).commit()
    }
}