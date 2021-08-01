package com.example.android.politicalpreparedness.ui_screens.election

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.data_source.network.models.Election
import com.example.android.politicalpreparedness.data_source.network.models.ElectionResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val electionsRepository: ElectionsRepository
) : ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<Result<ElectionResponse>>()
    val upcomingElections = _upcomingElections

    //TODO: Create live data val for saved elections
    private val _savedElections = MutableLiveData<Result<List<Election>>>(Result.Fetching)
    val savedElections = _savedElections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    fun getUpcomingElections() {
        viewModelScope.launch(dispatcher) {
            upcomingElections.postValue(Result.Fetching)

            try {
                upcomingElections.postValue(Result.Success(electionsRepository.getUpcomingElections()))
            } catch (exception: Exception) {
                upcomingElections.postValue(Result.Error(exception.message ?: ""))
            }
        }
    }

    fun getSavedElections() {
        viewModelScope.launch(dispatcher) {
            savedElections.postValue(Result.Fetching)

            try {
                savedElections.postValue(Result.Success(electionsRepository.getSavedElections()))
            } catch (exception: Exception) {
                savedElections.postValue(Result.Error(exception.message ?: ""))
            }
        }
    }
}