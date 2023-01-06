package com.example.appsophos.features.auth.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.MainActivity
import com.example.appsophos.core.APIClient
import com.example.appsophos.core.APIClient.getRetrofit
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.features.auth.domain.User
import com.example.appsophos.prefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//@HiltViewModel
//class LoginViewModel @Inject constructor(private val api: ApiService) : ViewModel() {
class LoginViewModel  : ViewModel() {
    val userName = MutableLiveData<String>()
    val idUsuario = MutableLiveData<String>()
    lateinit var inputEmail : String

    fun loginFun(email: String, password: String) {
        viewModelScope.launch (Dispatchers.IO) {
            inputEmail = email
            val user = getRetrofit().fetchUserInfo(email, password)
            val body = user.execute().body()


            if (!body?.nombre.isNullOrBlank() && body?.nombre.toString().isNotEmpty()) {
                userName.postValue(body?.nombre.toString())
                idUsuario.postValue(inputEmail)
                prefs.emailPref = inputEmail.toString()
                }
            else {
                userName.postValue("")
            }
        }
    }

}