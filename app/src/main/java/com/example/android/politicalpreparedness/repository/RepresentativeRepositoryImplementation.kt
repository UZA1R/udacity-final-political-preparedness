package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.data_source.network.CivicsApiService
import com.example.android.politicalpreparedness.data_source.network.models.Address

class RepresentativeRepositoryImplementation(private val api: CivicsApiService) :
    RepresentativeRepository {
    override suspend fun getAllRepresentatives(address: Address) =
        api.getRepresentatives(address.zip)
}