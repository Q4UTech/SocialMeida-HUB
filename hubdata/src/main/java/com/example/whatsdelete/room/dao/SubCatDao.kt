//package com.example.whatsdelete.room.dao
//
//import androidx.lifecycle.LiveData
//import androidx.room.*
//import com.example.whatsdelete.room.entity.SubCatEntity
//import java.time.ZoneId
//
//@Dao
//interface SubCatDao {
//
//    /*
//     1. Used CRUD (CREATE,READ,UPDATE,DELETE)
//     opration in DAO
//
//    2. used suspend fun for all CRUD fun working on BG thread
//
//3. LiveData used for READ function if func has LiveData return type than room doing bg thread by default.
//because LiveData is compatiable with room db.
//
//     */
//
//    @Insert
//    suspend fun insertSubCatData(contact: SubCatEntity)
//
//    @Update
//    suspend fun updateSubCatData(contact: SubCatEntity)
//
//    @Delete
//    suspend fun deleteSubCatData(contact: SubCatEntity)
//
//
//    @Query("SELECT * FROM subCatEntityTable")
//    fun getContact(): LiveData<List<SubCatEntity>>
//
//
//
//}