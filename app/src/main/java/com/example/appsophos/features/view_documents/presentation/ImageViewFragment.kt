package com.example.appsophos.features.view_documents.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R


class ImageViewFragment : Fragment() {
    private val viewModel: ViewDocsViewModel by viewModels()
    lateinit var backButton: Button
    var image: String = ""
    lateinit var idRegistro: String
    lateinit var imageHolder: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            idRegistro  = requireArguments().get("idRegister") as String
        }

        viewModel.getDocumentsById(idRegistro)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.documentbyIdRegistro.observe(viewLifecycleOwner, Observer {
            image = it.Adjunto
            Log.d("Main", "Imagen captada en string ${image}")
            if (!image.isNullOrEmpty() || image?.contains("9j")!! || image.isNotBlank()) {
                val imgConverted = convertString(image)
                imageHolder.setImageBitmap(imgConverted)
            }
            else {
                imageHolder.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
            }
        })

        imageHolder = view.findViewById(R.id.ivCardImage)
        backButton = view.findViewById(R.id.bBack)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_imageViewFragment_to_viewDocsFragment)
        }


    }

    fun convertString(imgString: String): Bitmap {
        val imageB64 = Base64.decode(imgString, Base64.DEFAULT)
        val processedString = BitmapFactory.decodeByteArray(imageB64, 0, imageB64.size)

        return processedString
    }




}