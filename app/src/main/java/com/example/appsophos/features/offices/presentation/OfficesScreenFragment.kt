package com.example.appsophos.features.offices.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.appsophos.R
import com.example.appsophos.features.offices.domain.Office
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

//@AndroidEntryPoint
//@Inject constructor()
class OfficesScreenFragment : Fragment(), OnMapReadyCallback {
    val viewModel: OfficesViewModel by viewModels()
    private lateinit var officesList: List<String>
    private  lateinit var appBar: MaterialToolbar
    private lateinit var officeInformation: Office
    lateinit var map : GoogleMap
    var cityName: String = "Dosquebradas"
    var cityAddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getOffices()
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

        viewModel.officeList.observe(viewLifecycleOwner, Observer {
            officesList = it.toSet().toList()
            if(!officesList.isNullOrEmpty()){
                setListOptions(officesList, requireView().findViewById(R.id.menuOffices))
            }
            else{
                val cities = listOf("Loading ...")
                setListOptions(cities, requireView().findViewById(R.id.menuOffices))
            }
        })

        setAppBar()
        createFragment()


      val selectedCity : AutoCompleteTextView = view.findViewById(R.id.atiCityLocation)
        selectedCity.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val cityValues: List<String> = selectedCity.adapter.getItem(position).toString().split(" (", ")")
            cityName = cityValues[0].toString()
            cityAddress = cityValues[1].toString()
            viewModel.getLocation(cityName, cityAddress)
        })
    }


    private fun createFragment() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        enableLocation()
        fun setMarker(coordinates : LatLng){
            map = googleMap
            map.addMarker(
                MarkerOptions()
                    .position(coordinates)
                    .title("Marker"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10F))
        }

        val coordinates = LatLng(4.845659641374458, -75.67218748983034)
        setMarker(coordinates)

        viewModel.officeInfo.observe(viewLifecycleOwner, Observer {
            officeInformation = it
            if(!officeInformation.Latitud.isNullOrEmpty() && !officeInformation.Longitud.isNullOrEmpty()) {
                val lat = officeInformation.Latitud.toDouble()
                val lng = officeInformation.Longitud.toDouble()
                val coordinates = LatLng(lat, lng)
                map.clear()
                setMarker(coordinates)
                val text = view?.findViewById<TextView>(R.id.tvMap)?.setText(officeInformation.Nombre)
            }
            else{
                val coordinates = LatLng(4.845659641374458, -75.67218748983034)
                setMarker(coordinates)
            }
        })
    }

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
            .setTitle(resources.getText(R.string.alert_title_spanish))
            .setMessage(resources.getString(R.string.alert_text_spanish))
            .setNeutralButton(resources.getString(R.string.alert_close_spanish)) { dialog, which ->
            }
            .show()
    }


}


