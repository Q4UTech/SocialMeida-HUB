//package com.example.whatsdelete.room.database
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.whatsdelete.room.dao.SubCatDao
//import com.example.whatsdelete.room.entity.SubCatEntity
//
///*
//
//1. in the database used entities as a array because you have may multiple entities.
//2. make abstract class and inherits with RoomDatabase
//3. link here our DAO Classes , craete a abstarct function and just call ContactDao and if you have multiple dao classes
//than crate multiple abstarct dao function
// */
//
//@Database(entities = [SubCatEntity::class], version = 1)
//abstract class CategoryDatabase : RoomDatabase() {
//
//    companion object {
//        //use Volatile for if INSTANCE update than all thread are instance aware with changes.
//        @Volatile
//        private var INSTANCE: CategoryDatabase? = null
//        fun getDatabase(context: Context) : CategoryDatabase {
//
//            if (INSTANCE ==null){
//                synchronized(this) {
//                    INSTANCE = Room.databaseBuilder(
//                        context.applicationContext, CategoryDatabase::class.java,
//                        "catdata_db"
//                    ).build()
//                }
//            }
//
//            return INSTANCE!!
//        }
//    }
//
//    abstract fun SubcatDAO(): SubCatDao
//}