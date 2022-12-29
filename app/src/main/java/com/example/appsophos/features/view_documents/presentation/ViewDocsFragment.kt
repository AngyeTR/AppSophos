package com.example.appsophos.features.view_documents.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.R
import com.example.appsophos.core.sharedPreferences.prefs
import com.example.appsophos.features.view_documents.domain.Document
import com.google.android.material.appbar.MaterialToolbar

class ViewDocsFragment : Fragment() {

    private val viewModel: ViewDocsViewModel by viewModels()
    private lateinit var adapter: DocumentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var appBar: MaterialToolbar
    lateinit var email : String
    lateinit var documentList : List<Document>
    lateinit var idRegister: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = prefs.emailPref.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_docs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun onItemClick(position: Int){
            idRegister = documentList[position].IdRegistro.toString()
            val bundle = Bundle()
            bundle.putString("idRegister", idRegister)

            findNavController().navigate(R.id.action_viewDocsFragment_to_imageViewFragment, bundle)
        }

        viewModel.getDocumentsByEmail(email)
        viewModel.documentByEmailModel.observe(viewLifecycleOwner, Observer {

            documentList = it
            if(!documentList.isNullOrEmpty()){
                recyclerView = view.findViewById(R.id.rvDocs)
                recyclerView.setHasFixedSize(true)
                adapter= DocumentAdapter(documentList){
                        position -> onItemClick(position)
                }
                recyclerView.adapter = adapter
            }
        })



        appBar = view.findViewById(R.id.topAppBar)
        appBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_viewDocsFragment_to_menuScreenFragment)
        }
        appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.send_option -> {
                    findNavController().navigate(R.id.action_viewDocsFragment_to_sendDocsFragment)
                    true
                }
                R.id.offices_option -> {
                    findNavController().navigate(R.id.action_viewDocsFragment_to_officesScreenFragment)
                    true
                }
                else -> false
            }
        }
    }
}

