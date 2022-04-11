package com.binaracademy.notesapp.database.dao

import androidx.room.*
import com.binaracademy.notesapp.database.UserDatabase
import com.binaracademy.notesapp.model.User

@Dao
interface user_dao {

    @Query("SELECT username From User Where username=(:user) AND password=(:pass)")
    fun login(user:String,pass:String) : String


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User): Long

    @Update
    fun updateUser(user: User):Int

    @Delete
    fun deleteUser(user: User):Int
}