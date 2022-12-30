package com.example.appsophos.features.view_documents.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.core.APIClient
import com.example.appsophos.features.view_documents.domain.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewDocsViewModel : ViewModel() {
    lateinit var email: String
    lateinit var idRegistro: String
    val documentByEmailModel = MutableLiveData<List<Document>>()
    val documentbyIdRegistro = MutableLiveData<Document>()
    val encodedString = MutableLiveData<Bitmap>()

    fun getDocumentsByEmail(email: String) {
        viewModelScope.launch (Dispatchers.IO) {
            val documents = APIClient.service.fetchDocumentsByEmail(email)
            val documentList = documents.execute().body()!!.Items
            documentByEmailModel.postValue(documentList)
            Log.d("Main", documentList.toString() )
        }
    }
    fun getDocumentsById(idRegistro: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val documents = APIClient.service.fetchDocumentsById(idRegistro)
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