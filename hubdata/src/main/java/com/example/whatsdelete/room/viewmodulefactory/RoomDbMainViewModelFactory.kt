//package com.example.whatsdelete.room.viewmodulefactory
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.whatsdelete.room.repositry.SubCatRepository
//
//class RoomDbMainViewModelFactory(private val repository: SubCatRepository) : ViewModelProvider.Factory {
//
//
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//       return RoomDbMainViewModel(repository) as T
//    }
//}