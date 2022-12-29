package com.example.appsophos.features.send_documents.domain

data class DocumentAdd(
    var TipoId: String,
    var Identificacion: String,
    var Nombre: String,
    var Apellido: String,
    var Ciudad : String,
    var Correo: String,
    var TipoAdjunto: String,
    val Adjunto: String,
)
