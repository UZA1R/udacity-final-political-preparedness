package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.data_source.database.ElectionDao
import com.example.android.politicalpreparedness.data_source.network.CivicsApiService
import com.example.android.politicalpreparedness.data_source.network.models.Election

class ElectionsRepositoryImplementation(
    private val electionDao: ElectionDao,
    private val civicsApiService: CivicsApiService
) : ElectionsRepository {
    override suspend fun getUpcomingElections() = civicsApiService.getUpcomingElections()
    override suspend fun getVoterInfo(address: String, electionId : Int) = civicsApiService.getVoterInfo(address, electionId)
    override suspend fun getRepresentatives(address: String, includeOffices: Boolean) =
        civicsApiService.getRepresentatives(address, true)

    override suspend fun getSavedElections() = electionDao.getAllSavedElections()
    override suspend fun saveElection(election: Election) = electionDao.saveElection(election)
    override suspend fun getAllSavedElections() = electionDao.getAllSavedElections()
    override suspend fun getElectionBy(electionId: Int) = electionDao.getElectionBy(electionId)
    override suspend fun removeElection(id: Int) = electionDao.removeElection(id)
}