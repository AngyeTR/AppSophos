package com.example.appsophos.features.send_documents.domain

data class DocumentAdd(
    val TipoId: String,
    val Identificacion: String,
    val Nombre: String,
    val Apellido: String,
    val Ciudad : String,
    val Correo: String,
    val TipoAdjunto: String,
    val Adjunto: String,
)
