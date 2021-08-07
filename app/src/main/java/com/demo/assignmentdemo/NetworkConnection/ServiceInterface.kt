package com.demo.assignmentdemo.NetworkConnection

import com.demo.assignmentdemo.Models.PhotosModel
import retrofit2.http.GET

interface ServiceInterface {
    @GET("photos")
    suspend fun getPhotoList() : List<PhotosModel>
}