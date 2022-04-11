package com.binaracademy.notesapp.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.blue
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binaracademy.notesapp.MainActivity
import com.binaracademy.notesapp.R
import com.binaracademy.notesapp.database.NotesDatabase
import com.binaracademy.notesapp.databinding.FragmentHomeBinding
import com.binaracademy.notesapp.model.Notes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class HomeFragment : Fragment() {


    lateinit var binding: FragmentHomeBinding
    var db: NotesDatabase? = null
    var listNotes: List<Notes>? = null
    var title = ""
    var activity = ""
    var adapter : HomeAdapter? = null

    private val sharedPref = "sharedpreferences"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = NotesDatabase.getInstance(requireContext())
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNotes.layoutManager = layoutManager
        binding.rvNotes.adapter = listNotes?.let { HomeAdapter(it) }
        binding.rvNotes.adapter?.notifyDataSetChanged()

        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        val username = sharedPreferences.getString("username","")
        binding.tvWelcome.setText("Welcome, ${username}")

        fetchData()


        //Custom Dialog
        val view =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null, false)
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setView(view)
        dialog.setCancelable(true)
        val dialogRead = dialog.create()
        val etTitle = view.findViewById<TextView>(R.id.etTitle)
        val etActivity = view.findViewById<TextView>(R.id.etActivity)
        val btnOk = view.findViewById<Button>(R.id.btnOk)

        binding.rvNotes.adapter.let {

        }

        binding.btnAdd.setOnClickListener {

            dialogRead.show()

            btnOk.setOnClickListener {
                title = etTitle.text.toString()
                activity = etActivity.text.toString()

                val notes = Notes(
                    null,
                    title,
                    activity
                )

                if (etTitle.text.isEmpty() || etActivity.text.isEmpty()){
                    Toast.makeText(requireContext(),"Silahkan isi kolom",Toast.LENGTH_LONG).show()
                }else{
                    val thread = Thread {
                        val result = db?.NotesDao()?.insertNotes(notes)
                        getActivity()?.runOnUiThread {
                            if (result != 0.toLong()) {
                                Toast.makeText(
                                    requireContext(),
                                    "Sukses Menambahkan Activity",
                                    Toast.LENGTH_LONG
                                ).show()

                                fetchData()

                                etTitle.setText("")
                                etActivity.setText("")

                                dialogRead.dismiss()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal Menambahkan Activity",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                   thread.start()
                }



            }


        }

        binding.btnRefresh.setOnClickListener{
            fetchData()
        }

        binding.tvLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Exit!")
                .setMessage("You sure wanna exit?")
                .setCancelable(true)
                .setPositiveButton("Yes"){
                        dialog, _ ->  editor.putBoolean("loginPref",false)
                    editor.apply()
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                }
                .setNegativeButton("Back"){
                        dialog,_ -> dialog.dismiss()
                }
                .show()
                .getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE)




        }

    }


    override fun onResume() {
        super.onResume()
        print("On Resume")
        Log.d("TAG","ON RESUME")
        fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    fun fetchData() {
       Thread {
            val listStudent = db?.NotesDao()?.getAllNotes()

            getActivity()?.runOnUiThread {
                listStudent?.let {
                    adapter = HomeAdapter(it)
                    binding.rvNotes.adapter = adapter
                    binding.rvNotes.adapter?.notifyDataSetChanged()


                }
            }

        }.start()
    }



}