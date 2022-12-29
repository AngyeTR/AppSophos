package com.example.appsophos.features.send_documents.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.core.APIClient
import com.example.appsophos.features.offices.domain.Office
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class SendDocsFragment : Fragment() {
    private  lateinit var appBar: MaterialToolbar
    private lateinit var offices: TextInputLayout
    private lateinit var textField: TextInputLayout
    private lateinit var officesList: List<Office>

    private fun getOffices() = thread {
        Log.d("Main", "pre validación")
        val offices = APIClient.service.getOfficesInfo()
        Log.d("Main", "primera validación")
        val body = offices.execute().body()
        Log.d("Main", "Segunda validación")
        if (body?.Items != null)
        {   Log.d("Main", body.toString())
            officesList = body.Items
        }
        else {
            Log.d("Main", "validación del else")
        }
    }

    val docExample = DocumentAdd (
        TipoId = "Cédula de Ciudadanía",
        Identificacion= "1151958439",
        Nombre= "Angie Tatiana",
        Apellido = "Rodriguez Gallego",
        Ciudad = "Cali",
        Correo= "Angye95@utp.edu.co",
        TipoAdjunto= "Incapacidad",
        Adjunto= "123456")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_docs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDocument(docExample)

        val menuDocTypes = listOf("Cédula de Ciudadanía", "Tarjeta de Identidad", "Pasaporte", "Cédula de extranjería")
        val adapterMenuDocType = ArrayAdapter(requireContext(), R.layout.list_item, menuDocTypes)
        textField = view.findViewById(R.id.menuDocType)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapterMenuDocType)

        val cities = listOf("Chile", "Estados Unidos", "Bogotá", "Medellín", "Bogotá - Tequendama",
            "Panamá", "Medellín - Ciudad del Río", "México")
        val adapterCities = ArrayAdapter(requireContext(), R.layout.list_item, cities)
        textField = view?.findViewById(R.id.city)!!
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

    private fun addDocument(body: DocumentAdd) {

        lifecycleScope.launch(Dispatchers.IO) {
            val retrofit = APIClient.service.addDocument(body)
            //val document = retrofit.execute().body()
            Log.d("Main", retrofit.message())
            //if (document != null) {

            //}
            //else Log.d("Main", "error")
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendDocsFragment().apply {
            }
    }
}