package com.binaracademy.notesapp.database.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.binaracademy.notesapp.model.Notes

@Dao
interface notes_dao {

    @Query("SELECT * From Notes")
    fun getAllNotes(): List<Notes>

    @Insert(onConflict = REPLACE)
    fun insertNotes(notes : Notes): Long

    @Update
    fun updateNotes(notes: Notes):Int

    @Delete
    fun deleteNotes(notes: Notes):Int
}