package com.example.android.politicalpreparedness.ui_screens.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.data_source.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.ui_screens.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

private const val TAG = "RepresentativeFragment"

class RepresentativeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: FragmentRepresentativeBinding

    //TODO: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        val viewModelFactory = RepresentativeViewModelFactory()
        ViewModelProvider(this, viewModelFactory).get(RepresentativeViewModel::class.java)
    }

    @SuppressLint("MissingPermission")
    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                Toast.makeText(
                    requireContext(),
                    R.string.location_permission_denied_explanation,
                    Toast.LENGTH_LONG
                ).show()
            } else {
                getLocation()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Establish bindings

        //TODO: Define and assign Representative adapter
        val adapter = RepresentativeListAdapter()
        binding.list.adapter = adapter

        //TODO: Populate Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Fetching -> {
                    showLoading()
                }
                is Result.Success -> {
                    binding.progress.visibility = View.INVISIBLE

                    adapter.addHeaderAndSubmitList(
                        getString(R.string.title_my_representatives),
                        it.data
                    )

                    binding.executePendingBindings()
                }
                is Result.Error -> {
                    showError()
                }
            }
        }

        //TODO: Establish button listeners for field and location search
        binding.buttonFindMyRepresentative.setOnClickListener {
            hideKeyboard()

            var address: Address? = null
            val addressFields = listOf(
                binding.addressLine1.text.toString(),
                binding.addressLine2.text.toString(),
                binding.city.text.toString(),
                binding.zip.text.toString()
            )

            for (field in addressFields) {
                if (field.isNotBlank()) {
                    address = geoCodeLocation(field)

                    if (address != null) {
                        binding.address = address
                        break
                    }
                }
            }

            if (address == null) {
                showInvalidAddressError()
            } else {
                viewModel.getAllRepresentatives(binding.address!!)
            }
        }
        binding.buttonUseMyLocation.setOnClickListener {
            hideKeyboard()

            //Check if the location permission is granted
            if (isPermissionGranted()) {
                //Get user's current location
                getLocation()
            } else {
                permissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        return binding.root
    }

    private fun isPermissionGranted() =
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    binding.address = geoCodeLocation(it)

                    //Get all the representatives after getting the location result
                    viewModel.getAllRepresentatives(binding.address!!)
                }
            }
    }

    //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun geoCodeLocation(location: String): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            return geocoder.getFromLocationName(location, 1)
                .map { address ->
                    Address(
                        address.thoroughfare ?: "",
                        address.subThoroughfare ?: "",
                        address.locality ?: "",
                        address.adminArea ?: "",
                        address.postalCode ?: ""
                    )
                }
                .first()
        } catch (exception: IOException) {
            showInvalidAddressError()
            Log.e(TAG, exception.message, exception)
        }

        return null
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.progress.visibility = View.INVISIBLE
        Toast.makeText(context, getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show()
    }

    private fun showInvalidAddressError() {
        Toast.makeText(context, getString(R.string.error_invalid_address), Toast.LENGTH_SHORT)
            .show()
    }
}