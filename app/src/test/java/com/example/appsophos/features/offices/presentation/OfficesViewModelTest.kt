package com.example.appsophos.features.offices.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.di.NetworkModule
import com.example.appsophos.features.auth.presentation.LoginViewModel
import com.example.appsophos.features.offices.domain.Office
import com.example.appsophos.features.offices.domain.Offices
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
import java.lang.reflect.TypeVariable

@ExperimentalCoroutinesApi
class OfficesViewModelTest {

    @RelaxedMockK
    private lateinit var api: ApiService

    lateinit var officesViewModel: OfficesViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)

        api = NetworkModule.provideRetrofit()
        officesViewModel = OfficesViewModel(api)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getOffices function returns a list of office names`() = runTest {
        officesViewModel.getOffices()
        var value = officesViewModel.officeList.getOrAwaitValue()
        assert(value is List )
    }

    @Test
    fun `getLocation function with a city name and address returns an Office Object`() = runTest {
        officesViewModel.getLocation("Medellín", "CEOH - 107")
        var value = officesViewModel.officeInfo.getOrAwaitValue()
        assert(value is Office)
    }
}