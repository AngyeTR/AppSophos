package com.example.appsophos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.databinding.CardViewDocsBinding


class DocumentAdapter(private val documents: List<Document>): RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view_docs, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val document  = documents[position]
        holder.title.text = "${document.TipoAdjunto}"
        holder.user.text = "${document.Nombre} ${document.Apellido}"

    }

    override fun getItemCount() =  documents.size


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView = itemView.findViewById(R.id.tvViewCardTitle)
        val user: TextView = itemView.findViewById(R.id.tvViewCardUser)

        }

}


