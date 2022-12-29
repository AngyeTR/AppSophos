package com.example.appsophos.features.send_documents.presentation

import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.core.APIClient
import com.example.appsophos.features.offices.domain.Offices
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import com.example.appsophos.features.view_documents.domain.Document
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendDocsViewModel : ViewModel() {
    lateinit var document: DocumentAdd
    val officeList = MutableLiveData<List<String>>()

    fun postDocument(document: DocumentAdd) {
        viewModelScope.launch (Dispatchers.IO) {
            val documents = APIClient.service.addDocument(document)
        }
    }

    fun getOffices() {
        viewModelScope.launch (Dispatchers.IO) {
            val offices = APIClient.service.getOfficesInfo()
            val body = offices.execute().body()!!.Items
            val officeNames = body.map { body -> "${body.Ciudad.toString()} - ${body.Nombre.toString()}" }
            officeList.postValue(officeNames)
        }
    }



}