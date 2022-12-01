package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Transformations.map
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject


private const val REQUEST_CODE_BACKGROUND = 102929
private const val REQUEST_TURN_DEVICE_LOCATION_ON = 12433
private const val DEFAULT_ZOOM_LEVEL =15f
class SelectLocationFragment : BaseFragment() {

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var map: GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1
    var Poi: PointOfInterest? = null
    var lat: Double = 0.0
    var long: Double = 0.0
    var title = ""
    var isLocationSelected = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

       val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
      mapFrag.getMapAsync(this)
        binding.savebut.setOnClickListener {
            if (Poi != null) {
                onLocationSelected()
            } else {
                Toast.makeText(context, "Select the wanted location!", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun onLocationSelected() {

        _viewModel.longitude.value = lat
        _viewModel.latitude.value = long
        _viewModel.selectedPOI.value = Poi

        _viewModel.reminderSelectedLocationStr.value = title
        _viewModel.navigationCommand.postValue(NavigationCommand.Back)

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID

            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE

            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN

            true
        }
        else -> super.onOptionsItemSelected(item)
    }
    override fun onMapReady(p0: GoogleMap) {
        map = p0
        setPoiClick(map)
        //        DONE: add style to the map
        setMapStyle(map)
        locationPermission()
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener {
            val poiMarker = map.addMarker(MarkerOptions().position(it.latLng).title(it.name))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(it.latLng, DEFAULT_ZOOM_LEVEL))
            poiMarker.showInfoWindow()
            Poi = it
            lat = it.latLng.latitude
            long = it.latLng.longitude
            title = it.name
        }
        isLocationSelected = true

    }


}
