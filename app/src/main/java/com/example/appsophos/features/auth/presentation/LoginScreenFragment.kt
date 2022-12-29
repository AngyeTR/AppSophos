package com.example.appsophos.features.auth.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.R.id.*
import com.example.appsophos.core.sharedPreferences.SharedApp
import com.example.appsophos.core.sharedPreferences.prefs
import java.util.prefs.Preferences


class LoginScreenFragment : Fragment() {
    private lateinit var btnLogin: Button
    var userName = ""
    private lateinit var inputEmail: com.google.android.material.textfield.TextInputEditText
    private lateinit var email: String
    private lateinit var inputPassword: com.google.android.material.textfield.TextInputEditText
    private lateinit var password: String

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userModel.observe(viewLifecycleOwner, Observer {
            userName = it.nombre
            prefs.emailPref = email
            if (userName.isNotBlank() || !userName.isNullOrEmpty() ) {
                prefs.namePref = userName
                findNavController().navigate(R.id.action_loginScreenFragment_to_menuScreenFragment)
            }
            else {
                Toast.makeText(activity?.applicationContext, "Information is wrong", Toast.LENGTH_SHORT).show()
            }
        } )

        inputEmail = view.findViewById(R.id.tiLoginEmail)
        inputPassword = view.findViewById(R.id.tiLoginPassword)

        btnLogin = view.findViewById(cbLogin)
        btnLogin.setOnClickListener {
            email = inputEmail.text.toString().lowercase()
            password = inputPassword.text.toString()

            if(email == "" || password == "") {
                Toast.makeText(activity?.applicationContext, "Enter your email and password", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.d("Main", "entre fun y toast")
                viewModel.loginFun(email, password)

            }
        }
    }
}