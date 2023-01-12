package com.example.appsophos.features.view_documents.presentation

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.R
import com.example.appsophos.features.view_documents.domain.Document

class DocumentAdapter(
    private val documents: List<Document>,
    private val onItemClicked:  (position: Int) -> Unit ) :
    RecyclerView.Adapter<DocumentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.card_view_docs, parent, false)

        return DocumentViewHolder(view, onItemClicked)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val item = documents[position]
        holder.render(item)
    }

    override fun getItemCount() =  documents.size
}


