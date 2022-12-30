package com.example.appsophos.features.send_documents.presentation

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.features.send_documents.domain.DocumentAdd
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputLayout
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SendDocsFragment : Fragment() {
    private  lateinit var appBar: MaterialToolbar
    private lateinit var textField: TextInputLayout
    private lateinit var officesList: List<String>
    lateinit var inputDocType : AutoCompleteTextView
    lateinit var inputDocNumber: com.google.android.material.textfield.TextInputEditText
    lateinit var inputName: com.google.android.material.textfield.TextInputEditText
    lateinit var inputLastName : com.google.android.material.textfield.TextInputEditText
    lateinit var inputEmail: com.google.android.material.textfield.TextInputEditText
    lateinit var inputCity: AutoCompleteTextView
    lateinit var inputAttachType: AutoCompleteTextView
    lateinit var inputCamera: ImageButton
    lateinit var inputDoc: Button
    lateinit var sendBtn: Button
    lateinit var document: DocumentAdd
    var infoIsComplete = true
    var processedString: String = ""

    private val viewModel : SendDocsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getOffices()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_send_docs, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputDocType = view.findViewById(R.id.actvDocType)
        inputDocNumber = view.findViewById(R.id.tiDocNumber)
        inputName = view.findViewById(R.id.tiName)
        inputLastName = view.findViewById(R.id.tiLastName)
        inputEmail = view.findViewById(R.id.tiEmail)
        inputCity = view.findViewById(R.id.atiCity)
        inputAttachType= view.findViewById(R.id.atiDocType)
        inputCamera= view.findViewById(R.id.ibAddPhoto)
        inputDoc = view.findViewById(R.id.cbLoadDoc)
        sendBtn = view.findViewById(R.id.cbSend)
        inputCamera = view.findViewById(R.id.ibAddPhoto)


        inputCamera.setOnClickListener(){
            startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        val menuDocTypes = listOf("CC", "TI", "PA", "CE")
        setListOptions(menuDocTypes, view.findViewById(R.id.menuDocType))

        viewModel.officeList.observe(viewLifecycleOwner, Observer {
            officesList = it.toSet().toList()
            if(!officesList.isNullOrEmpty()){
                setListOptions(officesList, view.findViewById(R.id.city))
            }
            else{
                val cities = listOf("Loading ...")
                setListOptions(cities, view.findViewById(R.id.city))
            }
        })

        val documentTypes = listOf("Incapacidad", "Solicitud", "Reporte", "Documentos Personales",
            "Comprobante de Pago", "PQR", "Otro")
        setListOptions(documentTypes, view.findViewById(R.id.tfDocType))

        setAppBar()

        inputDoc.setOnClickListener{
            pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }

        viewModel.encodedImg.observe(viewLifecycleOwner, Observer {
            processedString = it
        })

        viewModel.bitMapImage.observe(viewLifecycleOwner, Observer {
            inputCamera.setImageBitmap(it)
        })

        sendBtn.setOnClickListener(){
            document = getValues()

            if (infoIsComplete){
                Log.d("Main", "info Completa ${infoIsComplete} ${document}")
                addDocument(document)
                Toast.makeText(activity?.applicationContext, "Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_sendDocsFragment_to_menuScreenFragment)
            }
            else {
                Log.d("Main", "Info incompleta ${infoIsComplete} ${document}")
                Toast.makeText(activity?.applicationContext, "Information is not complete", Toast.LENGTH_SHORT).show()
                infoIsComplete = true
            }
        }

    }


    private fun addDocument(body: DocumentAdd) {
        viewModel.postDocument(body)
    }

    fun setAppBar(){
        appBar = requireView().findViewById(R.id.topAppBar)
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

    fun setListOptions(list: List<String>, textField: TextInputLayout) {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, list)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    fun getValues() : DocumentAdd {

        val docExample = DocumentAdd(
            TipoId = inputDocType.text.toString(),
            Identificacion = inputDocNumber.text.toString(),
            Nombre = inputName.text.toString(),
            Apellido = inputLastName.text.toString(),
            Correo = inputEmail.text.toString().lowercase(),
            Ciudad = inputCity.text.toString(),
            TipoAdjunto = inputAttachType.text.toString(),
            Adjunto = processedString
        )

        infoIsComplete = !(docExample.TipoId.isNullOrEmpty() || docExample.Identificacion.isNullOrEmpty() || docExample.Nombre.isNullOrEmpty() ||
                docExample.Apellido.isNullOrEmpty() || docExample.Correo.isNullOrEmpty() || docExample.Ciudad.isNullOrEmpty() || docExample.TipoAdjunto.isNullOrEmpty()
                || docExample.Adjunto.isNullOrEmpty())

        return docExample
    }

    @RequiresApi(Build.VERSION_CODES.P)
    val pickImage = registerForActivityResult(PickVisualMedia()){ uri ->
        if(uri != null) {
            val uriImg = uri
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uriImg)
            viewModel.convertUriToBitmap(source)
        }
        else{
            Log.d("Main", "Imagen no seleccionada")
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val startForResult =
        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
               val intentImage = result.data
                if (intentImage != null) {
                    viewModel.convertIntentToBitmap(intentImage)
                }
            }
            else {
                Log.d("Main", "Imagen no tomada")
            }
        }
}


