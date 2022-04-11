package com.binaracademy.notesapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.binaracademy.notesapp.R
import com.binaracademy.notesapp.database.UserDatabase
import com.binaracademy.notesapp.databinding.FragmentLoginBinding
import com.binaracademy.notesapp.databinding.FragmentRegisterBinding
import com.binaracademy.notesapp.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {


    private var user_db : UserDatabase? = null
    lateinit var binding : FragmentRegisterBinding

    var username =""
    var email =""
    var password =""
    var repassword =""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        user_db = UserDatabase.getInstance(requireContext())

        binding.btnRegister.setOnClickListener {
            username = binding.etUsername.text.toString()
            email = binding.etEmail.text.toString()
            password = binding.etPassword.text.toString()
            repassword = binding.etConfirmPassword.text.toString()

            val userList = User(
                null,
                username,
                email,
                password,
                repassword
            )

            if(username.isEmpty() || email.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                Toast.makeText(requireContext(),"Silahkan isi kolom terlebih dahulu",Toast.LENGTH_LONG).show()
            }else{
                Thread {
                    val result = user_db?.UserDao()?.insertUser(userList)
                    activity?.runOnUiThread{
                        if (result != 0.toLong()) {
                            Toast.makeText(
                                requireContext(),
                                "Sukses Menambahkan ${userList.username}",
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Gagal menambahkan ${userList.username}",
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
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }


}