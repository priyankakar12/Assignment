package com.demo.assignmentdemo.Repository

import androidx.lifecycle.LiveData
import com.demo.assignmentdemo.Database.UserDb
import com.demo.assignmentdemo.Database.UserModelRoomDb

class UserRoomRepository(private val userdb: UserDb) {
    suspend fun insertUser(user : UserModelRoomDb) = userdb.getUserDao().insertUser(user)

    fun getAllUserList () : LiveData<List<UserModelRoomDb>> = userdb.getUserDao().getAllUser()

    fun getSelectUserById(id : Int) = userdb.getUserDao().getSelectedUserById(id)

    suspend fun deleteUser (user: UserModelRoomDb) = userdb.getUserDao().deleteUser(user)

}