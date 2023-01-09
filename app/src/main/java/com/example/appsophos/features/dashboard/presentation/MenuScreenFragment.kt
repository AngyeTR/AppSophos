package com.example.appsophos.features.dashboard.presentation

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.SharedApp.Companion.prefs
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

class MenuScreenFragment: Fragment() {
    private lateinit var userName : String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName = prefs?.namePref.toString()
        var lang = prefs?.langPref
        if(lang.equals("es")) {
            setLang("es")
        }
         else {
            setLang("en")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_menu_screen, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSendDocs = view.findViewById<Button>(R.id.ob_send)
        val btnViewDocs = view.findViewById<Button>(R.id.ob_view)
        val btnOffices = view.findViewById<Button>(R.id.ob_office)
        setAppBar()

        btnSendDocs.setOnClickListener {
            findNavController().navigate(R.id.action_menuScreenFragment_to_sendDocsFragment)
        }

        btnViewDocs.setOnClickListener{
            findNavController().navigate(R.id.action_menuScreenFragment_to_viewDocsFragment)
        }

        btnOffices.setOnClickListener{
            findNavController().navigate(R.id.action_menuScreenFragment_to_officesScreenFragment)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setAppBar(){
        val appBar = view?.findViewById<MaterialToolbar>(R.id.topAppBar)
        appBar?.title = userName
        appBar?.setOnMenuItemClickListener{ menuItem ->
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
                    true
                }
                R.id.lang_option -> {
                    var lang = prefs?.langPref
                   if(lang.equals("es")) {
                       setLang("en")
                    }
                    else {
                        setLang("es")
                    }
                    findNavController().navigate(R.id.action_menuScreenFragment_self)
                    true
                }
                R.id.close_option -> {
                    findNavController().navigate(R.id.action_menuScreenFragment_to_loginScreenFragment)
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