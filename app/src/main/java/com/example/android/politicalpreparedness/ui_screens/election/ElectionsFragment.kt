package com.example.android.politicalpreparedness.ui_screens.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding

class ElectionsFragment : Fragment() {

    //TODO: Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        val viewModelFactory = ElectionsViewModelFactory()
        ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root

        //TODO: Add ViewModel values and create ViewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters

    }

    //TODO: Refresh adapters when fragment loads

}