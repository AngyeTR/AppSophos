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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//@AndroidEntryPoint
class ImageViewFragment : Fragment() {
    val viewModel: ViewDocsViewModel by viewModels()
    lateinit var backButton: Button
    var image: String = ""
    lateinit var idRegistro: String
    lateinit var imageHolder: ImageView
    lateinit var textHolder: TextView
    lateinit var convertedImage: Bitmap

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
            image = it.Adjunto.toString()
            if (!image.isNullOrEmpty() && image?.contains("9j")!! && image.isNotBlank()) {
                //val imgConverted = convertString(image)
                viewModel.convertString(image)

                //imageHolder.setImageBitmap(imgConverted)
                //textHolder.setText("")
            }
            else {
                imageHolder.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                textHolder.setText(R.string.send_img_unavailable_spanish)

            }
        })

        viewModel.encodedString.observe(viewLifecycleOwner, Observer{
            imageHolder.setImageBitmap(it)
            textHolder.setText("")
        })

        imageHolder = view.findViewById(R.id.ivCardImage)
        textHolder = view.findViewById(R.id.tvCardImage)
        backButton = view.findViewById(R.id.bBack)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_imageViewFragment_to_viewDocsFragment)
        }
    }
}