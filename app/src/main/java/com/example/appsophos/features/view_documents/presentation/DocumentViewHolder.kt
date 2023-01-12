package com.example.appsophos.features.view_documents.presentation

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.R
import com.example.appsophos.features.view_documents.domain.Document
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(document: Document){
        val unformattedDate = document.Fecha.toString()
        val date = formatDate(unformattedDate)
        title.text = "${date} ${document.TipoAdjunto}"
        user.text = "${document.Nombre} ${document.Apellido}"

    }

    fun formatDate(preDate: String): String{
        var date = preDate
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(date)
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
            dateFormatter.timeZone = TimeZone.getDefault()
            date = dateFormatter.format(value)
        } catch (e: Exception) {
            date = "00-00-0000"
        }
        return date.toString()
    }

}

