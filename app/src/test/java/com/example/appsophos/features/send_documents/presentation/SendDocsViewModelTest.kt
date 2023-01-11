package com.example.appsophos.features.send_documents.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.di.NetworkModule
import com.example.appsophos.features.auth.presentation.LoginViewModel
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import com.example.appsophos.features.view_documents.domain.Document
import com.example.appsophos.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SendDocsViewModelTest {

    @RelaxedMockK
    private lateinit var api: ApiService

    lateinit var sendDocsViewModel: SendDocsViewModel
    lateinit var document : DocumentAdd

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)


        api = NetworkModule.provideRetrofit()
        sendDocsViewModel = SendDocsViewModel(api)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `postDocument function with complete data returns true`() = runTest {
        //Given
        document = DocumentAdd("Cedula", "115195843","Angie", "Rodriguez", "Bogotá", "angye95@utp.edu.co", "Reporte", "ImgBase64")
        //When
        sendDocsViewModel.postDocument(document)
        var value =sendDocsViewModel.apiResponse.getOrAwaitValue()
        //Then
        assert(value == true)
    }

    @Test
    fun `postDocument function with incomplete data returns false`() = runTest {
        //Given
        document = DocumentAdd("", "","", "", "", "", "", "")
        //When
        sendDocsViewModel.postDocument(document)
        var value =sendDocsViewModel.apiResponse.getOrAwaitValue()
        //Then
        assert(value == false)
    }



}