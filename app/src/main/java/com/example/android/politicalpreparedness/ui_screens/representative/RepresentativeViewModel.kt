package com.example.android.politicalpreparedness.ui_screens.representative

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.data_source.network.models.Address
import com.example.android.politicalpreparedness.ui_screens.representative.model.Representative

class RepresentativeViewModel : ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _representatives = MutableLiveData<Representative>()
    private val representatives = _representatives

    private val _address = MutableLiveData<Address>()
    private val address = _address

    //TODO: Create function to fetch representatives from API from a provided address
    fun getAllRepresentatives() {
    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    fun getCombined() {
        val (offices, officials) = getRepresentativesDeferred.await()
        _representatives.value = offices.flatMap
        { office ->
            office.getRepresentatives(officials)
        }
    }

    //TODO: Create function get address from geo location
    fun getAddressFromGeoLocation()//    : Address
    {
//        return Address()
    }

    //TODO: Create function to get address from individual fields
    fun getAddressFrom()
//    : Address
    {
//        return Address()
    }
}
