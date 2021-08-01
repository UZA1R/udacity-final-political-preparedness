package com.example.android.politicalpreparedness.data_source.network.models

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "election_table")
@Parcelize
data class Election(
    @PrimaryKey val id: Int,
    val name: String,
    val electionDay: Date,
    @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
) : Parcelable