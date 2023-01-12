package com.example.appsophos.core.services.remote

import com.example.appsophos.features.view_documents.domain.Documents
import com.example.appsophos.features.auth.domain.User
import com.example.appsophos.features.offices.domain.Offices
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("RS_Usuarios")
    fun fetchUserInfo(@Query("idUsuario") idUsuario : String,@Query("clave") clave: String) : Call<User>

    @GET("RS_Documentos")
    fun fetchDocumentsByEmail(@Query("correo") correo: String): Call<Documents>

    @GET("RS_Documentos")
    fun fetchDocumentsById(@Query("idRegistro") idRegistro: String): Call<Documents>

    @POST("RS_Documentos")
    suspend fun addDocument(@Body document: DocumentAdd) : Response<Unit>

    @GET("RS_Oficinas")
    fun getOfficesInfo(): Call<Offices>

    @GET("RS_Oficinas")
    fun getOfficebyName(@Query("ciudad") ciudad: String) : Call<Offices>


}