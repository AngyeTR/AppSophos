package com.example.appsophos.features.send_documents.presentation

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.Source
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsophos.core.services.remote.ApiService
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SendDocsViewModel @Inject constructor(private val api: ApiService) : ViewModel() {
//class SendDocsViewModel : ViewModel() {
    lateinit var document: DocumentAdd
    val officeList = MutableLiveData<List<String>>()
    val encodedImg= MutableLiveData<String>()
    var bitMapImage = MutableLiveData<Bitmap>()
    var documentToPost = MutableLiveData<DocumentAdd>()
    var processedString: String = ""
    var apiResponse = MutableLiveData<Boolean>()

    fun postDocument(document: DocumentAdd) {
        viewModelScope.launch (Dispatchers.IO) {
            val documents = api.addDocument(document)
            apiResponse.postValue(documents.isSuccessful)
        }
    }

    fun getOffices() {
        viewModelScope.launch (Dispatchers.IO) {
            val offices = api.getOfficesInfo()
            val body = offices.execute().body()!!.Items
            val officeNames = body.map { body -> body.Ciudad.toString() }
            officeList.postValue(officeNames)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun convertUriToBitmap(source: Source){
        viewModelScope.launch (Dispatchers.IO) {
            val image = ImageDecoder.decodeBitmap(source)
            bitMapImage.postValue(image)
            processedString = encodeImage(image)?.filter { !it.isWhitespace() }.toString()
            encodedImg.postValue(processedString)
        }
    }

    fun convertIntentToBitmap(intentImage: Intent){
        viewModelScope.launch (Dispatchers.IO) {
            val uriImg = intentImage?.extras?.get("data") as Bitmap
            bitMapImage.postValue(uriImg)
            processedString = encodeImage(uriImg)?.filter { !it.isWhitespace() }.toString()
            encodedImg.postValue(processedString)
        }
    }

    fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val ba = baos.toByteArray()
        return android.util.Base64.encodeToString(ba, android.util.Base64.DEFAULT)
    }

    fun createDocument(inputDocType: String, inputDocNumber: String, inputName: String, inputLastName: String, inputEmail: String, inputCity: String, inputAttachType: String, attachment: String) {
        val docExample = DocumentAdd(
            TipoId = inputDocType,
            Identificacion = inputDocNumber,
            Nombre = inputName,
            Apellido = inputLastName,
            Correo = inputEmail,
            Ciudad = inputCity,
            TipoAdjunto = inputAttachType,
            Adjunto = attachment)
        documentToPost.postValue(docExample)
    }


}
