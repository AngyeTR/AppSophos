package com.example.appsophos.features.send_documents.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appsophos.prefs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

//@AndroidEntryPoint
class SendDocsFragment : Fragment() {
    val viewModel : SendDocsViewModel by viewModels()
    private lateinit var textField: TextInputLayout
    private lateinit var officesList: List<String>
    var infoIsComplete = true
    var processedString: String = ""
    var  attachment: String = ""

    private val PERMISSION_CAMARA: Int = 100
    private val PERMISSION_EXTERNAL_STORAGE: Int = 100

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

        val inputCamera= view.findViewById<ImageButton>(R.id.ibAddPhoto)
        val inputDoc = view.findViewById<Button>(R.id.cbLoadDoc)
        val sendBtn = view.findViewById<Button>(R.id.cbSend)

        setAppBar()
        setDropDownMenus()

        viewModel.encodedImg.observe(viewLifecycleOwner, Observer {
            processedString = it
        })

        viewModel.bitMapImage.observe(viewLifecycleOwner, Observer {
            inputCamera.setImageBitmap(it)
        })

        inputCamera.setOnClickListener(){
            askForCameraPermission()
        }

        inputDoc.setOnClickListener{
            askForFilesPermission()
        }

        sendBtn.setOnClickListener(){
            getValues()
        }
    }

    private fun setAppBar(){
        val appBar = requireView().findViewById<MaterialToolbar>(R.id.topAppBar)
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
                R.id.close_option -> {
                    findNavController().navigate(R.id.action_sendDocsFragment_to_loginScreenFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun setDropDownMenus(){
        val menuDocTypes = listOf("CC", "TI", "PA", "CE")
        setListOptions(menuDocTypes, requireView().findViewById(R.id.menuDocType))

        val documentTypes = listOf("Incapacidad", "Solicitud", "Reporte", "Documentos Personales",
            "Comprobante de Pago", "PQR", "Otro")
        setListOptions(documentTypes, requireView().findViewById(R.id.tfDocType))

        viewModel.officeList.observe(viewLifecycleOwner, Observer {
            officesList = it.toSet().toList()
            if(!officesList.isNullOrEmpty()){
                setListOptions(officesList, requireView().findViewById(R.id.city))
            }
            else{
                val cities = listOf("Loading ...")
                setListOptions(cities, requireView().findViewById(R.id.city))
            }
        })
    }

    fun setListOptions(list: List<String>, textField: TextInputLayout) {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, list)
        (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
    fun getValues()  {
        val inputDocType = view?.findViewById<AutoCompleteTextView>(R.id.actvDocType)?.text.toString()
        val inputDocNumber = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiDocNumber)?.text.toString()
        val inputName = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiName)?.text.toString()
        val inputLastName = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiLastName)?.text.toString()
        val inputEmail = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.tiEmail)?.text.toString().lowercase()
        val inputCity = view?.findViewById<AutoCompleteTextView>(R.id.atiCity)?.text.toString()
        val inputAttachType= view?.findViewById<AutoCompleteTextView>(R.id.atiDocType)?.text.toString()

        attachment = processedString.toString()

        infoIsComplete = !(inputDocType.isNullOrEmpty() || inputDocNumber.isNullOrEmpty() || inputName.isNullOrEmpty() ||
                inputLastName.isNullOrEmpty() || inputEmail.isNullOrEmpty() || inputCity.isNullOrEmpty() || inputAttachType.isNullOrEmpty()
                || attachment.isNullOrEmpty())

        if (infoIsComplete){
            viewModel.createDocument(inputDocType, inputDocNumber, inputName, inputLastName, inputEmail, inputCity, inputAttachType,  attachment)
            viewModel.documentToPost.observe(viewLifecycleOwner, Observer{
                val document: DocumentAdd
                document = it
                viewModel.postDocument(document)
            })
            Toast.makeText(activity?.applicationContext, "Success", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_sendDocsFragment_to_menuScreenFragment)
        }
        else {
            Toast.makeText(activity?.applicationContext, R.string.send_info_not_complete_spanish, Toast.LENGTH_SHORT).show()
            infoIsComplete = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    val pickImage = registerForActivityResult(PickVisualMedia()){ uri ->
        if(uri != null) {
            val uriImg = uri
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uriImg)
            viewModel.convertUriToBitmap(source)
        }
        else{
            Toast.makeText(activity?.applicationContext, R.string.send_image_not_loaded_spanish, Toast.LENGTH_SHORT).show()
            infoIsComplete = true
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
                Toast.makeText(activity?.applicationContext, R.string.send_image_not_capture_spanish, Toast.LENGTH_SHORT).show()
                infoIsComplete = true
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun askForCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                capture()
            }
            shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) -> {
               setDialog()
            }
            else -> {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PERMISSION_CAMARA)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun askForFilesPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                load()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                setDialog()
            }
            else -> {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_EXTERNAL_STORAGE
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   capture()
                }
            }
            PERMISSION_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    load()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun capture(){
        startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun load(){
        pickImage.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    fun setDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.alert_permission_title_spanish)
            .setMessage(R.string.alert_permission_message_spanish)
            .setNeutralButton(R.string.alert_close_spanish) { dialog, which ->
            }
            .show()
    }
}


