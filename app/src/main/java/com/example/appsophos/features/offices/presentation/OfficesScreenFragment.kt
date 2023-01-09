package com.example.appsophos.features.offices.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.SharedApp
import com.example.appsophos.features.offices.domain.Office
import com.example.appsophos.prefs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class OfficesScreenFragment : Fragment(), OnMapReadyCallback {
    val viewModel: OfficesViewModel by viewModels()
    private lateinit var officesList: List<String>
    private  lateinit var appBar: MaterialToolbar
    private lateinit var officeInformation: Office
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var map : GoogleMap
    var cityName: String = ""
    var cityAddress: String = ""
    private lateinit var list : List<Office>

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getOffices()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offices_screen, container, false)

        createFragment()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.officeList.observe(viewLifecycleOwner, Observer {
            officesList = it.toSet().toList()
            if(officesList.isNotEmpty()){
                setListOptions(officesList, requireView().findViewById(R.id.menuOffices))
            }
            else{
                val cities = listOf("Loading ...")
                setListOptions(cities, requireView().findViewById(R.id.menuOffices))
            }
        })

        setAppBar()

        val selectedCity : AutoCompleteTextView = view.findViewById(R.id.atiCityLocation)
        selectedCity.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val cityValues: List<String> = selectedCity.adapter.getItem(position).toString().split(" (", ")")
            cityName = cityValues[0]
            cityAddress = cityValues[1]
            viewModel.getLocation(cityName, cityAddress)
        })
    }

    private fun createFragment() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableLocation()
        setCurrentLocation()

        viewModel.officeInfo.observe(viewLifecycleOwner, Observer {
            officeInformation = it
            if(officeInformation.Latitud.isNotEmpty() && officeInformation.Longitud.isNotEmpty()) {
                val lat = officeInformation.Latitud.toDouble()
                val lng = officeInformation.Longitud.toDouble()
                val coordinates = LatLng(lat, lng)
                map.clear()
                setMarker(coordinates)
                view?.findViewById<TextView>(R.id.tvMap)?.setText(officeInformation.Nombre)
            }
            else{
                setCurrentLocation()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setAppBar(){
        appBar = requireView().findViewById(R.id.topAppBar)
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
                R.id.mode_option -> {
                    var darkMode = SharedApp.prefs?.modePref
                    if(darkMode == true){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        SharedApp.prefs?.modePref = false
                    }
                    else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        SharedApp.prefs?.modePref = true
                    }
                    true
                }
                R.id.lang_option -> {
                    var lang = SharedApp.prefs?.langPref
                    if (lang.equals("es")) {
                        setLang("en")
                    } else {
                        setLang("es")
                    }
                    findNavController().navigate(R.id.action_officesScreenFragment_self)
                    true
                }
                R.id.close_option -> {
                    findNavController().navigate(R.id.action_officesScreenFragment_to_loginScreenFragment)
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

    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isPermissionGranted()) {
            map.isMyLocationEnabled
        } else {
            requestLocationPermission()
        }
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )) {
            setDialog()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled
            } else {
                setDialog()
            }
            else -> {}
        }
    }

    fun setDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.alert_permission_title_spanish)
            .setMessage(R.string.alert_permission_message_spanish)
            .setNeutralButton(R.string.alert_close_spanish) { dialog, which ->
                findNavController().navigate(R.id.action_officesScreenFragment_to_menuScreenFragment)
            }
            .show()
    }

    fun setMarker(coordinates : LatLng){
        map.addMarker(
            MarkerOptions()
                .position(coordinates)
                .title("Sophos Office"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10F))
    }

    @SuppressLint("MissingPermission")
    private fun setCurrentLocation(){
        lateinit var closestOffice: String

        var coordinate = LatLng(0.0, 0.0)
        viewModel.officeCoordinates.observe(viewLifecycleOwner, Observer {
            list = it

            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    if (location != null) {
                        coordinate = LatLng(location.latitude, location.longitude)
                        Log.d("Main", coordinate.toString())
                        setMarker(coordinate)
                        viewModel.findClosestOffice(list, location.latitude, location.longitude)
                        viewModel.closestLocation.observe(viewLifecycleOwner, Observer {
                            closestOffice = it
                            val text = "${resources.getText(R.string.office_closest_office)} ${closestOffice}"
                            view?.findViewById<TextView>(R.id.tvMap)?.setText(text)
                        })
                    }
                    else{
                        setMarker(coordinate)
                        closestOffice = "None"
                    }
                }
        })
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


