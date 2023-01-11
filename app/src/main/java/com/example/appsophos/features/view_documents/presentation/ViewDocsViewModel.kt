package com.example.appsophos.features.view_documents.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.core.APIClient.getRetrofit
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.features.view_documents.domain.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewDocsViewModel @Inject constructor(private val api: ApiService) : ViewModel() {

    lateinit var email: String
    lateinit var idRegistro: String
    val documentByEmailModel = MutableLiveData<List<Document>>()
    val documentbyIdRegistro = MutableLiveData<Document>()
    val encodedString = MutableLiveData<Bitmap>()

    fun getDocumentsByEmail(email: String) {
        val em = email
        viewModelScope.launch (Dispatchers.IO) {
            val documents = api.fetchDocumentsByEmail(email)
            val documentList = documents.execute().body()!!.Items
            documentByEmailModel.postValue(documentList)
        }
    }
    fun getDocumentsById(idRegistro: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val documents = api.fetchDocumentsById(idRegistro)
            val document = documents.execute().body()!!.Items[0]
            documentbyIdRegistro.postValue(document)
        }
    }

    fun convertString(imgString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageB64 = Base64.decode(imgString, Base64.DEFAULT)
            val processedString = BitmapFactory.decodeByteArray(imageB64, 0, imageB64.size)
            encodedString.postValue(processedString)
        }
    }

}