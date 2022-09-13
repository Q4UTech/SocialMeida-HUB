//package com.example.whatsdelete.room.viewmodulefactory
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.whatsdelete.room.entity.SubCatEntity
//import com.example.whatsdelete.room.repositry.SubCatRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class RoomDbMainViewModel(private val repository: SubCatRepository) : ViewModel() {
//
//    fun getQuotes() : LiveData<List<SubCatEntity>>{
//        return repository.getQuotesData()
//    }
//
//    fun insertQuotes(quoteEntity: SubCatEntity){
//       viewModelScope.launch(Dispatchers.IO) {
//           repository.insertQuotes(quoteEntity)
//
//       }
//    }
//}