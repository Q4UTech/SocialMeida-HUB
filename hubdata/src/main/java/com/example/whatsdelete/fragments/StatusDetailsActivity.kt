//package com.example.whatsdelete.fragments
//
//import android.os.Bundle
//import android.os.PersistableBundle
//import androidx.appcompat.app.AppCompatActivity
//import com.pds.hubdata.R
//
//class StatusDetailsActivity : AppCompatActivity() {
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContentView(R.layout.activity_main)
//
////        val args1 =  Bundle()
////        var id = args1?.get("id").toString()
////        var img = args1?.get("img_url").toString()
//
//
//        var id = intent.getStringExtra("id")
//        var img = intent.getStringExtra("img_url")
//
//        val ldf = DetailFragment()
//        val args = Bundle()
//        args.putString("id", id)
//        args.putString("img_url", img)
//        ldf.arguments = args
//        supportFragmentManager!!.beginTransaction().replace(R.id.container, ldf)
//            .addToBackStack(null).commit()
//    }
//}