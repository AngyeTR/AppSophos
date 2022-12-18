package com.example.appsophos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.features.view_documents.domain.Document
import com.example.appsophos.features.view_documents.presentation.DocumentAdapter

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showData(documents)

        }

    private fun showData(documents: List<Document>){
        var recyclerView = findViewById<RecyclerView>(R.id.rvDocs)
        recyclerView?.apply{
            var layoutManager = LinearLayoutManager(this@MainActivity)
            val adapter =
                DocumentAdapter(
                    documents
                )
        }
    }

    private val documents: List<Document> =
        listOf(
            Document("118", "12/10/2021","cc" , "12345", "Angie", "Rodriguez", "Cali", "no", "Incapacidad", "456" ),
            Document("118", "12/10/2021","cc" , "12345", "Tatiana", "Gallego", "Cali", "no", "Cedula", "456" ),
            Document("118", "12/10/2021", "cc" , "12345", "Camilo", "Rodriguez", "Cali", "no", "Permiso", "456" ),
            Document( "118", "12/10/2021","cc" , "12345", "Andres", "Campuzano", "Cali", "no", "Reporte", "456" ),
        )



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }


}