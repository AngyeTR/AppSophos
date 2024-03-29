package com.example.appsophos.features.view_documents.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.appsophos.R
import com.example.appsophos.SharedApp
import com.example.appsophos.SharedApp.Companion.prefs
import com.example.appsophos.features.view_documents.domain.Document
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ViewDocsFragment : Fragment() {
   val viewModel: ViewDocsViewModel by viewModels()
    private lateinit var adapter: DocumentAdapter
    private lateinit var recyclerView: RecyclerView
    lateinit var email : String
    lateinit var documentList : List<Document>
    lateinit var idRegister: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = prefs?.emailPref.toString()
        viewModel.getDocumentsByEmail(email)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_docs, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBar()

        fun onItemClick(position: Int){
            idRegister = documentList[position].IdRegistro.toString()
            val bundle = Bundle()
            bundle.putString("idRegister", idRegister)
            findNavController().navigate(R.id.action_viewDocsFragment_to_imageViewFragment, bundle)
        }

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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setAppBar(){
        val appBar = view?.findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar?.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_viewDocsFragment_to_menuScreenFragment)
        }
        appBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                R.id.send_option -> {
                    findNavController().navigate(R.id.action_viewDocsFragment_to_sendDocsFragment)
                    true
                }
                R.id.offices_option -> {
                    findNavController().navigate(R.id.action_viewDocsFragment_to_officesScreenFragment)
                    true
                }
                R.id.mode_option -> {
                    var darkMode = prefs?.modePref
                    if(darkMode == true){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        prefs?.modePref = false
                    }
                    else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        prefs?.modePref = true
                    }
                    findNavController().navigate(R.id.action_menuScreenFragment_self)
                    true
                }
                R.id.lang_option -> {
                    var lang = SharedApp.prefs?.langPref
                    if (lang.equals("es")) {
                        setLang("en")
                    } else {
                        setLang("es")
                    }
                    findNavController().navigate(R.id.action_viewDocsFragment_self)
                    true
                }
                R.id.close_option -> {
                    findNavController().navigate(R.id.action_viewDocsFragment_to_loginScreenFragment)
                    true
                }
                else -> false
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun setLang(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, requireContext().resources.displayMetrics)
        prefs?.langPref = lang
    }
}

