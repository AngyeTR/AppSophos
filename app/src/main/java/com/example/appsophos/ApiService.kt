package com.example.appsophos

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("RS_Usuarios")
    fun fetchUserInfo(@Query("idUsuario") idUsuario : String,@Query("clave") clave: String) : Call<User>

    @GET("RS_Documentos")
    fun fetchDocuments(@Query("idRegistro") idRegistro : String): Call<Documents>

    @POST("RS_Documentos")
    fun addDocument(@Body document: Document) : Call<Document>

}