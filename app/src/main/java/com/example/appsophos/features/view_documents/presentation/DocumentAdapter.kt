package com.example.appsophos.features.view_documents.presentation

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.R
import com.example.appsophos.core.APIClient
import com.example.appsophos.features.offices.domain.Office
import com.example.appsophos.features.view_documents.domain.Document
import org.w3c.dom.Text
import kotlin.concurrent.thread

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


