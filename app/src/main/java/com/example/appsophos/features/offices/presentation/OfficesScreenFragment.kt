package com.example.appsophos.features.offices.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.google.android.material.appbar.MaterialToolbar

class OfficesScreenFragment : Fragment() {
    private  lateinit var appBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_offices_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = view.findViewById(R.id.topAppBar)
        appBar.setNavigationOnClickListener{
            findNavController().navigate(R.id.action_officesScreenFragment_to_menuScreenFragment)
        }
        appBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId) {

                R.id.send_option -> {
                    findNavController().navigate(R.id.action_officesScreenFragment_to_sendDocsFragment)
                    true
                }
                R.id.view_option -> {
                    findNavController().navigate(R.id.action_officesScreenFragment_to_viewDocsFragment)
                    true
                }
                else -> false
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OfficesScreenFragment().apply {
            }
    }
}