package com.example.appsophos.features.auth.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.MainActivity
import com.example.appsophos.core.APIClient
import com.example.appsophos.features.auth.domain.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    val userModel = MutableLiveData<User>()
    val idUsuario = MutableLiveData<String>()
    lateinit var email : String

    fun loginFun(email: String, password: String) {
        viewModelScope.launch (Dispatchers.IO) {
            val user = APIClient.service.fetchUserInfo(email, password)
            val body = user.execute().body()
            userModel.postValue(body!!)

            this@LoginViewModel.email = email
            Log.d("Main", "checking loginFun ${email}")
            idUsuario.postValue(email)
        }
    }

}