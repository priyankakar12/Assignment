package com.demo.assignmentdemo.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.assignmentdemo.Models.PhotosModel
import com.demo.assignmentdemo.NetworkConnection.RetrofitBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotosViewModel : ViewModel() {
    private var photos:MutableLiveData<List<PhotosModel>> = MutableLiveData()
    fun getPhotoList() : MutableLiveData<List<PhotosModel>>{
        return photos
    }
    fun initializeCall(){
        viewModelScope.launch(Dispatchers.IO) {
            val response= RetrofitBase.apiInterface.getPhotoList()
            photos.postValue(response)
        }
    }
}