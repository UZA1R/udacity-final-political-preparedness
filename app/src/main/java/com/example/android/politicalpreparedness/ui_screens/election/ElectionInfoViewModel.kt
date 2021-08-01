package com.example.android.politicalpreparedness.ui_screens.election

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.data_source.network.models.Election
import com.example.android.politicalpreparedness.data_source.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

private const val TAG = "ElectionInfoViewModel"

class ElectionInfoViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val repository: ElectionsRepository
) : ViewModel() {
    //TODO: Add live data to hold voter info
    private val _buttonState = MutableLiveData<ButtonState>(ButtonState.UnFollowed)
    val buttonState = _buttonState

    private val _electionInfo = MutableLiveData<Result<VoterInfoResponse>>()
    val electionInfo = _electionInfo
    lateinit var election: Election

    //TODO: Add var and methods to populate voter info

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    fun followElection(election: Election) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.saveElection(election)
                _buttonState.postValue(ButtonState.Followed)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    fun unfollowElection(election: Election) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.removeElection(election.id)
                _buttonState.postValue(ButtonState.UnFollowed)
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    fun getElectionStatus(election: Election) {
        viewModelScope.launch(dispatcher) {
            try {
                val savedElection = repository.getElectionBy(election.id)
                if (savedElection != null && savedElection.id > -1) {
                    _buttonState.postValue(ButtonState.Followed)
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    fun getElectionInfo(election: Election) {
        viewModelScope.launch(dispatcher) {
            try {
                _electionInfo.postValue(Result.Fetching)

                _electionInfo.postValue(Result.Success(repository.getVoterInfo(election.name, election.id)))
            } catch (exception: Exception) {
                _electionInfo.postValue(Result.Error(exception.message ?: ""))
                Log.e(TAG, exception.message, exception)
            }
        }
    }

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}

sealed class ButtonState {
    object Followed : ButtonState()
    object UnFollowed : ButtonState()
}