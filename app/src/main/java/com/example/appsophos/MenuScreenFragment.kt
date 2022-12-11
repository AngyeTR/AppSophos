package com.example.appsophos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar

class MenuScreenFragment : Fragment() {
    private lateinit var btnSendDocs: Button
    private lateinit var btnViewDocs: Button
    private lateinit var btnOffices: Button
    private  lateinit var appBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = view.findViewById(R.id.topAppBar)
        appBar.setOnMenuItemClickListener{ menuItem ->
            when (menuItem.itemId) {
                R.id.send_option -> {
                    findNavController().navigate(R.id.action_menuScreenFragment_to_sendDocsFragment)
                    true
                }
                R.id.view_option -> {
                    findNavController().navigate(R.id.action_menuScreenFragment_to_viewDocsFragment)
                    true
                }
                R.id.offices_option -> {
                    findNavController().navigate(R.id.action_menuScreenFragment_to_officesScreenFragment)
                    true
                }
                else -> false
            }
        }

        btnSendDocs = view.findViewById(R.id.ob_send)
        btnSendDocs.setOnClickListener {
            findNavController().navigate(R.id.action_menuScreenFragment_to_sendDocsFragment)
        }

        btnViewDocs = view.findViewById(R.id.ob_view)
        btnViewDocs.setOnClickListener{
            findNavController().navigate(R.id.action_menuScreenFragment_to_viewDocsFragment)
        }

        btnOffices = view.findViewById(R.id.ob_office)
        btnOffices.setOnClickListener{
            findNavController().navigate(R.id.action_menuScreenFragment_to_officesScreenFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuScreenFragment().apply {
            }
    }
}