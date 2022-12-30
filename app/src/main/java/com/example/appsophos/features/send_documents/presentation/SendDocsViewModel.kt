package com.example.appsophos.features.send_documents.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.Source
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import java.io.ByteArrayOutputStream


class SendDocsViewModel : ViewModel() {
    lateinit var document: DocumentAdd
    val officeList = MutableLiveData<List<String>>()
    val encodedImg= MutableLiveData<String>()
    var bitMapImage = MutableLiveData<Bitmap>()
    var processedString: String = ""

    fun postDocument(document: DocumentAdd) {
        viewModelScope.launch (Dispatchers.IO) {
            val documents = APIClient.service.addDocument(document)
        }
    }

    fun getOffices() {
        viewModelScope.launch (Dispatchers.IO) {
            val offices = APIClient.service.getOfficesInfo()
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
}
