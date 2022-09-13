//package com.example.whatsdelete.room.repositry
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import com.example.whatsdelete.responce.ApplicationListData
//import com.example.whatsdelete.responce.ApplicationListResponce
//import com.example.whatsdelete.room.dao.SubCatDao
//import com.example.whatsdelete.room.entity.SubCatEntity
//
//
//// here use dao instance for performing action
//class SubCatRepository(private val quoteDAO: SubCatDao) {
//
//
//    private val quotesLiveData =MutableLiveData<ApplicationListData>()
//    val quotes : LiveData<ApplicationListData>
//    get() = quotesLiveData
//
//
//    fun getQuotesData(): LiveData<List<SubCatEntity>>{
//        return quoteDAO.getContact()
//    }
//
//    suspend fun insertQuotes(quoteEntity: SubCatEntity){
//        quoteDAO.insertSubCatData(quoteEntity)
//    }
//
//}