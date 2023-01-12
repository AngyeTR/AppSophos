package com.example.appsophos.features.view_documents.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.di.NetworkModule
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ViewDocsViewModelTest {

    @RelaxedMockK
    private lateinit var api: ApiService

    lateinit var viewModel: ViewDocsViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)


        api = NetworkModule.provideRetrofit()
        viewModel = ViewDocsViewModel(api)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get documents function with correct email returns a List of documents`() = runTest {
        //Given
        val email = "angye95@utp.edu.co"
        //When
        viewModel.getDocumentsByEmail(email)
        var value = viewModel.documentByEmailModel.getOrAwaitValue()
        //Then
        assert(value is List)
    }

    @Test
    fun `get document function with correct idRegistro returns a document`() = runTest {
        //Given
        val idRegistro = "119"
        //When
        viewModel.getDocumentsById(idRegistro)
        var value = viewModel.documentbyIdRegistro.getOrAwaitValue()
        //Then
        assert(value is Document)
    }
}