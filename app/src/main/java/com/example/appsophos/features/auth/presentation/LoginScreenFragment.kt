package com.example.appsophos.features.auth.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

@AndroidEntryPoint
class LoginScreenFragment  : Fragment() {

    val viewModel: LoginViewModel by viewModels()
    private lateinit var email: String
    private lateinit var password : String
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var canAuth: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkBiometricAvailability()
        var lang = prefs?.langPref
        if (lang.equals("es")) {
            setLang("es")
        } else {
            setLang("en")
        }
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
            try {
                viewModel.loginFun(email, password)
            } catch (e: Exception){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Error de red")
                    .setMessage("Ha ocurrido un error de red. Por favor verifique su conexión a internet. si este error persiste, por favor contacte a Soporte Tecnico")
                    .setNeutralButton(resources.getString(R.string.alert_close_spanish)) { dialog, which ->
                    }
                    .show()
            }
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
                    try { viewModel.loginFun(email, password)}
                    catch (e: RuntimeException){
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Error de red")
                            .setMessage("Ha ocurrido un error de red. Por favor verifique su conexión a internet. si este error persiste, por favor contacte a Soporte Tecnico")
                            .setNeutralButton(resources.getString(R.string.alert_close_spanish)) { dialog, which ->
                            }
                            .show()
                    }

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
                .setTitle(getString(R.string.login_bio_title_spanish))
                .setSubtitle(getString(R.string.login_bio_subtitle_spanish))
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
    private fun setTheme(){
        var darkMode = prefs?.modePref
        if(darkMode == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setLang(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        prefs?.langPref = lang
    }
}