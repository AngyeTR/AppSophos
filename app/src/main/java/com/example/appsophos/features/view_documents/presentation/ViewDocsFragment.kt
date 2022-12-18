package com.example.appsophos.features.view_documents.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.core.APIClient
import com.google.android.material.appbar.MaterialToolbar
import kotlin.concurrent.thread


class ViewDocsFragment : Fragment() {

    private lateinit var appBar: MaterialToolbar

    private fun viewFun() = thread {
        val documents = APIClient.service.fetchDocuments("118")
        val body = documents.execute().body( )
        if (body != null)
            Log.d("Main", body.toString())
        else Log.d("Main", "Error")
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

        viewFun()
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


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewDocsFragment().apply {
            }
    }

}

