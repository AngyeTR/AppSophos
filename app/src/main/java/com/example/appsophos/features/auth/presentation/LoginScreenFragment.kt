package com.example.appsophos.features.auth.presentation

import android.content.Intent
import android.os.Bundle

import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//@AndroidEntryPoint
class LoginScreenFragment  : Fragment() {

    val viewModel: LoginViewModel by viewModels()
    private lateinit var email: String
    private lateinit var password : String
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var canAuth: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkBiometricAvailability()
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
        viewModel.userName.observe(viewLifecycleOwner, Observer {
           val  userName = it
            if (userName.isBlank() || userName.isNullOrEmpty()) {
                Toast.makeText(activity?.applicationContext,  R.string.login_error_spanish, Toast.LENGTH_SHORT).show()
            }
            else {
                if(canAuth){
                    compareSession(userName)
                }
                prefs?.namePref = userName
                prefs?.emailPref = email
                findNavController().navigate(R.id.action_loginScreenFragment_to_menuScreenFragment)
            }
        } )

        val btnLogin = view.findViewById<Button>(cbLogin)
        btnLogin.setOnClickListener {
            sendInfo()
        }

        val btnFingerPrint : Button = view.findViewById(R.id.obLogin)
        btnFingerPrint.setOnClickListener(){
            if(canAuth){
                checkSharedPreferences()
            }
            else {
                Toast.makeText(activity?.applicationContext, R.string.login_bio_unavailable_spanish, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun sendInfo(){
        email = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiLoginEmail)?.text.toString().lowercase()
        password = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiLoginPassword)?.text.toString()
        if(email == "" || password == "") {
            Toast.makeText(activity?.applicationContext, R.string.login_directions_spanish, Toast.LENGTH_SHORT).show()
        }
        else {
            viewModel.loginFun(email, password)
        }
    }

    private fun fingerPrintAuth() {
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(requireContext(),
                        "${R.string.login_auth_error_spanish} $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    loadUserFingerPrintPreferences()
                    viewModel.loginFun(email, password)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(requireContext(), R.string.login_auth_error2_spanish,
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun checkBiometricAvailability() {
        if(BiometricManager.from(requireContext()).canAuthenticate(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS){
            canAuth = true
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(R.string.login_bio_title_spanish.toString())
                .setSubtitle(R.string.login_bio_subtitle_spanish.toString())
                .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
                .build()
        }
    }

    private fun checkSharedPreferences(){
        email = prefs?.emailPref.toString()
        password = prefs?.passwordPref.toString()
        if (email == "" || password == "") {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.login_bio_title_spanish)
                .setMessage(R.string.login_auth_first_spanish)
                .setNeutralButton(resources.getString(R.string.alert_close_spanish)) { dialog, which -> prefs?.namePref=""}
                .show()
        }
        else {
            fingerPrintAuth()
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun loadUserFingerPrintPreferences() {
        email = prefs?.emailPref.toString()
        password = prefs?.passwordPref.toString()
    }

    fun    compareSession(userName: String){
        if (!prefs?.namePref.toString().equals(userName)){
            setDialog()
        }
    }

    fun setDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.login_auth_saveInfo_title_spanish)
            .setMessage(R.string.login_auth_saveInfo_subtitle_spanish)
            .setNeutralButton(resources.getString(R.string.alert_close_spanish)) { dialog, which ->
            }.setPositiveButton(R.string.login_auth_saveInfo_btn_spanish){ dialog, which ->
                prefs?.passwordPref = password }
            .show()
    }
}