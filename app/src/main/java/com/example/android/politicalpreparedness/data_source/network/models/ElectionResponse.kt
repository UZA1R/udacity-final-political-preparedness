package com.example.android.politicalpreparedness.data_source.network.models

data class ElectionResponse(
    val kind: String,
    val elections: List<Election>
)