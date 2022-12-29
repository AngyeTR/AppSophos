package com.example.appsophos.features.view_documents.presentation

import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.R
import com.example.appsophos.features.view_documents.domain.Document
import com.google.android.material.card.MaterialCardView

class DocumentViewHolder(view: View,
    private val onItemClicked:  (position: Int) -> Unit)
    : RecyclerView.ViewHolder(view) {

    val title: TextView
    val user: TextView
    val card: MaterialCardView

    init {
        title = itemView.findViewById(R.id.tvViewCardTitle)
        user = itemView.findViewById(R.id.tvViewCardUser)
        card = itemView.findViewById(R.id.card)
        card.setOnClickListener {
            val position = adapterPosition
            onItemClicked(position)
        }
    }

    fun render(document: Document){
        title.text = "${document.Fecha} ${document.TipoAdjunto}"
        user.text = "${document.Nombre} ${document.Apellido}"

    }

}

