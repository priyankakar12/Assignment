package com.demo.assignmentdemo.Database


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "user_db")
@Parcelize
data class UserModelRoomDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val address: String?,
    val byteArray: ByteArray?): Parcelable
