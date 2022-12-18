package com.example.appsophos.features.auth.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.R.id.action_loginScreenFragment_to_menuScreenFragment
import com.example.appsophos.core.APIClient
import kotlin.concurrent.thread

class LoginScreenFragment : Fragment() {
    private lateinit var btnLogin: Button
    private lateinit var userName: String

    @SuppressLint("SuspiciousIndentation")
    private fun loginFun() = thread {
        val user = APIClient.service.fetchUserInfo("angye95@utp.edu.co", "vdYc38kG85V2")
        val body = user.execute().body()
        if (body != null)
            Log.d("Main", body.apellido.toString())
            userName =  body!!.nombre.toString()
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

        btnLogin = view.findViewById(R.id.cbLogin)
        btnLogin.setOnClickListener {
            loginFun()
            findNavController().navigate(action_loginScreenFragment_to_menuScreenFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginScreenFragment().apply {
            }
    }
}