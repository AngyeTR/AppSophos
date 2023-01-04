package com.example.appsophos.features.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.hardware.biometrics.BiometricManager
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.R.id.*
import com.example.appsophos.SharedApp.Companion.prefs
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor
import javax.inject.Inject
import android.provider.Settings

//@AndroidEntryPoint
class LoginScreenFragment  : Fragment() {

    val viewModel: LoginViewModel by viewModels()
    var userName = ""
    private lateinit var email: String
    private lateinit var password : String
    //////////
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
     //////////////////////
        viewModel.userModel.observe(viewLifecycleOwner, Observer {
            userName = it.nombre
            prefs?.emailPref = email
            if (userName.isNotBlank() || !userName.isNullOrEmpty() ) {
                prefs?.namePref = userName
                findNavController().navigate(R.id.action_loginScreenFragment_to_menuScreenFragment)
            }
            else {
                Toast.makeText(activity?.applicationContext, "Information is wrong", Toast.LENGTH_SHORT).show()
            }
        } )

        val btnLogin = view.findViewById<Button>(cbLogin)
        btnLogin.setOnClickListener {
            sendInfo()
        }
////////////////////////////////////
    }

    fun sendInfo(){
        email = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiLoginEmail)?.text.toString().lowercase()
        val password: String = requireView().findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiLoginPassword).text.toString()
        if(email == "" || password == "") {
            Toast.makeText(activity?.applicationContext, "Enter your email and password", Toast.LENGTH_SHORT).show()
        }
        else {
            Log.d("Main", "entre fun y toast")
            viewModel.loginFun(email, password)
        }
    }
//////////////////////

}