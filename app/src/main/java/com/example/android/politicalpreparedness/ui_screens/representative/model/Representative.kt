package com.example.android.politicalpreparedness.ui_screens.representative.model

import com.example.android.politicalpreparedness.data_source.network.models.Office
import com.example.android.politicalpreparedness.data_source.network.models.Official

data class Representative (
        val official: Official,
        val office: Office
)