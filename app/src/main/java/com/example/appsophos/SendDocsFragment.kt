package com.example.appsophos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import kotlin.concurrent.thread

class SendDocsFragment : Fragment() {
    private  lateinit var appBar: MaterialToolbar
    private lateinit var textField: TextInputLayout
    val response: ResponseModel= ResponseModel(message = "Respuesta")

    val docExample: Document = Document (
        IdRegistro = "118",
        Fecha = "17/02/2022",
        TipoId = "Cédula de Ciudadanía",
        Identificacion= "1151958439",
        Nombre= "Angie Tatiana",
        Apellido = "Rodriguez Gallego",
        Ciudad = "Cali",
        Correo= "Angye95@utp.edu.co",
        TipoAdjunto= "Incapacidad",
        Adjunto= "123456")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun addDocument(body: Document, String: ResponseModel){
            val retrofit = APIClient.service.addDocument(body)
            val document = retrofit.execute().body()
            if (document != null) {
                Log.d("Main", document.Nombre)
            }
            else Log.d("Main", "error")
                }

    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_docs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuDocTypes = listOf("Cédula de Ciudadanía", "Tarjeta de Identidad", "Pasaporte", "Cédula de extranjería")
        val adapterMenuDocType = ArrayAdapter(requireContext(), R.layout.list_item, menuDocTypes)
        textField = view.findViewById(R.id.menuDocType)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapterMenuDocType)

        val cities = listOf("Chile", "Estados Unidos", "Bogotá", "Medellín", "Bogotá - Tequendama",
            "Panamá", "Medellín - Ciudad del Río", "México")
        val adapterCities = ArrayAdapter(requireContext(), R.layout.list_item, cities)
        textField = view.findViewById(R.id.city)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapterCities)

        val documentTypes = listOf("Incapacidad", "Solicitud", "Reporte", "Documentos Personales",
            "Comprobante de Pago", "PQR", "Otro")
        val adapterDocumentTypes = ArrayAdapter(requireContext(), R.layout.list_item, documentTypes)
        textField = view.findViewById(R.id.tfDocType)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapterDocumentTypes)



        appBar = view.findViewById(R.id.topAppBar)
        appBar.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_sendDocsFragment_to_menuScreenFragment)
        }
        appBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId) {

                R.id.view_option -> {
                    findNavController().navigate(R.id.action_sendDocsFragment_to_viewDocsFragment)
                    true
                }
                R.id.offices_option -> {
                    findNavController().navigate(R.id.action_sendDocsFragment_to_officesScreenFragment)
                    true
                }
                else -> false
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendDocsFragment().apply {
            }
    }
}