package com.example.appsophos.features.offices.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.core.APIClient.getRetrofit
import com.example.appsophos.features.offices.domain.Office
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.sqrt

class OfficesViewModel : ViewModel() {

    lateinit var document: DocumentAdd
    val officeList = MutableLiveData<List<String>>()
    val officeInfo = MutableLiveData<Office>()
    val closestLocation = MutableLiveData<String>()
    val officeCoordinates = MutableLiveData<List<Office>>()

    fun getOffices() {
        viewModelScope.launch(Dispatchers.IO) {
            val offices = getRetrofit().getOfficesInfo()
            val body = offices.execute().body()!!.Items
            val officeNames = body.map { body -> "${body.Ciudad} (${body.Nombre})" }
            officeList.postValue(officeNames)
            officeCoordinates.postValue(body)
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

    fun findClosestOffice(list: List<Office>, lat: Double, lng: Double){
        viewModelScope.launch(Dispatchers.IO) {
            if(list != null){
                var closer = sqrt((lat - list[0].Latitud.toDouble()).pow(2) +
                        (lng - list[0].Longitud.toDouble()).pow(2))
                var closerIndex = 0

                var i = 1
                while (i <= list.lastIndex){
                    val distance = sqrt((lat - list[i].Latitud.toDouble()).pow(2) +
                            (lng - list[i].Longitud.toDouble()).pow(2))
                    if(closer > distance)
                    { closer = distance
                        closerIndex = i
                        Log.d("Main", " ${closer}")}
                    i++
                }
                val closestOffice =  "${list[closerIndex].Ciudad}, ${list[closerIndex].Nombre}"
                closestLocation.postValue(closestOffice)
            }
        }
    }
}
