package com.binaracademy.notesapp.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.binaracademy.notesapp.R
import com.binaracademy.notesapp.database.NotesDatabase
import com.binaracademy.notesapp.database.UserDatabase
import com.binaracademy.notesapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {


    lateinit var binding : FragmentLoginBinding
    private var user_db : UserDatabase? = null
    private var notes_db : NotesDatabase? = null
    var username ="";
    var password =""
    private val sharedPref = "sharedpreferences"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        user_db = UserDatabase.getInstance(requireContext())
        notes_db = NotesDatabase.getInstance(requireContext())

        val sharedPreferences : SharedPreferences = requireActivity().getSharedPreferences(sharedPref, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.btnLogin.setOnClickListener {
            username = binding.etUsername.text.toString()
            password = binding.etPassword.text.toString()

            editor.putString("username",username)
            editor.putBoolean("loginPref",true)
            editor.apply()

            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(requireContext(),"Silahkan isi username dan password anda",Toast.LENGTH_LONG).show()
            }else{
                Thread {
                    val result = user_db?.UserDao()?.login(username,password)
                    activity?.runOnUiThread{
                        if (result != null) {
                            Toast.makeText(
                                requireContext(),
                                "Login berhasil",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Gagal Login ",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }.start()
            }


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }


}