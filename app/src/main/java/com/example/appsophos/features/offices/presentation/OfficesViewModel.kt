package com.example.appsophos.features.offices.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.core.APIClient.getRetrofit
import com.example.appsophos.features.offices.domain.Office
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OfficesViewModel : ViewModel() {

    lateinit var document: DocumentAdd
    val officeList = MutableLiveData<List<String>>()
    val officeInfo = MutableLiveData<Office>()

    fun getOffices() {
        viewModelScope.launch(Dispatchers.IO) {
            val offices = getRetrofit().getOfficesInfo()
            val body = offices.execute().body()!!.Items
            val officeNames = body.map { body -> "${body.Ciudad} (${body.Nombre})" }
            officeList.postValue(officeNames)
        }
    }

    fun getLocation(cityName:String, cityAddress: String){
        viewModelScope.launch(Dispatchers.IO) {
            val offices = getRetrofit().getOfficesInfo()
            val body = offices.execute().body()!!.Items
            val info = body.filter { it.Ciudad.toString().equals(cityName) && it.Nombre.toString().equals(cityAddress)}
            val office = info[0]

            officeInfo.postValue(office)

        }
    }
}