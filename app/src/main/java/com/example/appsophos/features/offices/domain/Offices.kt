package com.example.appsophos.features.offices.domain

import com.example.appsophos.features.view_documents.domain.Document

data class Offices(
    val Items: List<Office>,
    val Count: Int,
    val ScannedCount: Int
)
