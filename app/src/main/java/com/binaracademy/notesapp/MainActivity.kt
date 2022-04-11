package com.binaracademy.notesapp


import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.binaracademy.notesapp.database.NotesDatabase
import com.binaracademy.notesapp.database.UserDatabase
import com.binaracademy.notesapp.fragment.HomeAdapter
import com.binaracademy.notesapp.fragment.HomeFragment
import com.binaracademy.notesapp.model.Notes


class MainActivity : AppCompatActivity() {

    private var user_db: UserDatabase? = null
    private var notes_db: NotesDatabase? = null
    lateinit var list: List<Notes>
    lateinit var rv : RecyclerView
    lateinit var adapter : HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        user_db = UserDatabase.getInstance(this)
        notes_db = NotesDatabase.getInstance(this)


    }



}

