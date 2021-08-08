package com.demo.assignmentdemo.ViewModel

import androidx.lifecycle.ViewModel
import com.demo.assignmentdemo.Database.UserModelRoomDb
import com.demo.assignmentdemo.Repository.UserRoomRepository

class UserViewModel(private val userRoomRepository: UserRoomRepository) : ViewModel() {

    suspend fun insertUser (user : UserModelRoomDb) = userRoomRepository.insertUser(user)

    fun getAllUser() = userRoomRepository.getAllUserList()

    fun getSelectUserById(id : Int) = userRoomRepository.getSelectUserById(id)

    suspend fun deleteUser(user: UserModelRoomDb) = userRoomRepository.deleteUser(user)

}