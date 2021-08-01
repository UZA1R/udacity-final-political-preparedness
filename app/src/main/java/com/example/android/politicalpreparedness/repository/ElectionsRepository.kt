package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.data_source.network.models.Election
import com.example.android.politicalpreparedness.data_source.network.models.ElectionResponse
import com.example.android.politicalpreparedness.data_source.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.data_source.network.models.VoterInfoResponse

interface ElectionsRepository {
    suspend fun getUpcomingElections(): ElectionResponse
    suspend fun getVoterInfo(address: String, electionId : Int): VoterInfoResponse
    suspend fun getRepresentatives(
        address: String,
        includeOffices: Boolean = true
    ): RepresentativeResponse

    suspend fun getSavedElections(): List<Election>
    suspend fun saveElection(election: Election)
    suspend fun getAllSavedElections(): List<Election>
    suspend fun getElectionBy(electionId: Int): Election
    suspend fun removeElection(id: Int)
}