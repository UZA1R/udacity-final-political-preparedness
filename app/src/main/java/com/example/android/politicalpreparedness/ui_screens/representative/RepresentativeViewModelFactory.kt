package com.example.android.politicalpreparedness.ui_screens.representative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.data_source.network.CivicsApi
import com.example.android.politicalpreparedness.data_source.network.CivicsApiService
import com.example.android.politicalpreparedness.repository.RepresentativeRepositoryImplementation
import com.example.android.politicalpreparedness.ui_screens.election.ElectionInfoViewModel
import kotlinx.coroutines.Dispatchers

class RepresentativeViewModelFactory() :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            val dispatchers = Dispatchers.IO
            val civicsApiService: CivicsApiService = CivicsApi.retrofitService
            val repository = RepresentativeRepositoryImplementation(civicsApiService)
            return RepresentativeViewModel(dispatchers, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}