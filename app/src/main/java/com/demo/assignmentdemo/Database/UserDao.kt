package com.demo.assignmentdemo.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userModel: UserModelRoomDb)

    @Query("SELECT * FROM user_db")
    fun getAllUser(): LiveData<List<UserModelRoomDb>>

    @Query("SELECT * FROM user_db where id =:id")
    fun getSelectedUserById(id : Int) : LiveData<UserModelRoomDb>

    @Update
    suspend fun updateUser(userModel: UserModelRoomDb)

    @Delete
    suspend fun deleteUser(userModel: UserModelRoomDb)

}