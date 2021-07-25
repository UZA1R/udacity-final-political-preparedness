package com.example.android.politicalpreparedness.data_source.network.models

import com.example.android.politicalpreparedness.ui_screens.representative.model.Representative
import com.squareup.moshi.Json

data class Office (
    val name: String,
    @Json(name="divisionId") val division:Division,
    @Json(name="officialIndices") val officials: List<Int>
) {
    fun getRepresentatives(officials: List<Official>): List<Representative> {
        return this.officials.map { index ->
            Representative(officials[index], this)
        }
    }
}
