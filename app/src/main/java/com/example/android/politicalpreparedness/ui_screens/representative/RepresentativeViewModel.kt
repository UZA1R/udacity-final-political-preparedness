package com.example.android.politicalpreparedness.ui_screens.representative

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.data_source.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.ui_screens.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

private const val TAG = "RepresentativeViewModel"

class RepresentativeViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val repository: RepresentativeRepository
) : ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _representatives = MutableLiveData<Result<List<Representative>>>()
    val representatives = _representatives

    //TODO: Create function to fetch representatives from API from a provided address
    fun getAllRepresentatives(address: Address) {
        viewModelScope.launch(dispatcher) {
            try {
                _representatives.postValue(Result.Fetching)

                /**
                 *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

                val (offices, officials) = getRepresentativesDeferred.await()
                _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

                Note: getRepresentatives in the above code represents the method used to fetch data from the API
                Note: _representatives in the above code represents the established mutable live data housing representatives

                 */
                val (offices, officials) = repository.getAllRepresentatives(address)
                _representatives.postValue(Result.Success(offices.flatMap { office ->
                    office.getRepresentatives(officials)
                }))
            } catch (exception: Exception) {
                _representatives.postValue(Result.Error(exception.message ?: ""))
                Log.e(TAG, exception.message, exception)
            }
        }
    }
}
