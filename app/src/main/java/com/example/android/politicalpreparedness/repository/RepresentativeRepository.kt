package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.data_source.network.models.Address
import com.example.android.politicalpreparedness.data_source.network.models.RepresentativeResponse

interface RepresentativeRepository {
    suspend fun getAllRepresentatives(address: Address): RepresentativeResponse
}