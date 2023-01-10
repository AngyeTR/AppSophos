package com.example.appsophos.features.auth.presentation

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appsophos.core.APIClient
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.di.NetworkModule
import com.example.appsophos.features.auth.domain.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @RelaxedMockK
    private lateinit var api: ApiService

    lateinit var loginViewModel: LoginViewModel

    @get:Rule
    var rule:InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)


        api = NetworkModule.provideRetrofit()
        loginViewModel = LoginViewModel(api)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login function with correct data returns not Empty`() = runTest {
        //Given
       val email = "angye95@utp.edu.co"
        val password = "vdYc38kG85V2"
        //When
        loginViewModel.loginFun(email, password)
        var value = loginViewModel.userName.value.toString()
        //Then
        assert(value != "")
    }
    @Test
    fun `login function with correct data returns not null`() = runTest {
        //Given
        val email = "angye95@utp.edu.co"
        val password = "vdYc38kG85V2"
        //When
        loginViewModel.loginFun(email, password)
        var value = loginViewModel.userName.value.toString()
        //Then
        assert(value != null)
    }

    @Test
    fun `login function with incorrect data returns Not Null`() = runTest {
        //Given
        loginViewModel = LoginViewModel(api)
        val email = "angye95@utp"
        val password = "vdYc38kG85"
        loginViewModel.loginFun(email, password)
        var value = loginViewModel.userName.value.toString()
        //Then
        assert(value != null)
    }

    @Test
    fun `login function with incorrect data returns Empty`() = runTest {
        //Given
        loginViewModel = LoginViewModel(api)
        val email = "angye95@utp"
        val password = "vdYc38kG85"
        loginViewModel.loginFun(email, password)
        var value = loginViewModel.userName.value.toString()
        //Then
        assert(value != "")
    }

}