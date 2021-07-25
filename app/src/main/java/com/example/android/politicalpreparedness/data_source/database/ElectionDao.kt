package com.example.android.politicalpreparedness.data_source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.politicalpreparedness.data_source.network.models.Election

@Dao
interface ElectionDao {

    //TODO: Add insert query
    @Insert()
    fun saveElection(election: Election)

    //TODO: Add select all election query
    @Query("SELECT * FROM election_table")
    fun getAllElections(): List<Election>

    //TODO: Add select single election query
    @Query("SELECT * FROM election_table where id = :id LIMIT 1")
    fun getElectionBy(id: Int): Election

    //TODO: Add delete query
    @Query("DELETE FROM election_table where id = :id")
    fun removeElection(id: Int)

    //TODO: Add clear query
//    @Query("SELECT * FROM election_table where id = :id")
//    fun clearElection(id: Int)
}