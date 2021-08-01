package com.example.android.politicalpreparedness.ui_screens.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.ui_screens.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.ui_screens.election.adapter.OnClickListener

class ElectionsFragment : Fragment() {
    private lateinit var binding: FragmentElectionBinding

    //TODO: Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        val viewModelFactory = ElectionsViewModelFactory(requireContext())
        ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUpcomingElections()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel
        //TODO: Add binding values
        binding.electionsViewModel = viewModel

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters
        setUpUpcomingElectionsRecyclerView(binding)

        setUpSavedElectionsRecyclerView(binding)

        return binding.root
    }

    private fun setUpSavedElectionsRecyclerView(binding: FragmentElectionBinding) {
        val savedElectionsAdapter = ElectionListAdapter(OnClickListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    it
                )
            )
        })
        binding.recyclerViewSavedElections.adapter = savedElectionsAdapter

        viewModel.savedElections.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Fetching -> {
                    showLoading(Election.Saved)
                }
                is Result.Success -> {
                    binding.savedElectionsProgress.visibility = View.INVISIBLE
                    savedElectionsAdapter.addHeaderAndSubmitList(
                        getString(R.string.header_saved_elections),
                        it.data
                    )

                    binding.executePendingBindings()
                }
                is Result.Error -> {
                    showError(Election.Saved)
                }
            }
        }
    }

    private fun setUpUpcomingElectionsRecyclerView(binding: FragmentElectionBinding) {
        val upcomingElectionsAdapter = ElectionListAdapter(OnClickListener {
            findNavController().navigate(
                ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                    it
                )
            )
        })
        binding.recyclerViewUpcomingElections.adapter = upcomingElectionsAdapter

        //TODO: Populate recycler adapters
        viewModel.upcomingElections.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Fetching -> {
                    showLoading(Election.Upcoming)
                }
                is Result.Success -> {
                    binding.upcomingElectionsProgress.visibility = View.INVISIBLE
                    upcomingElectionsAdapter.addHeaderAndSubmitList(
                        getString(R.string.header_upcoming_elections),
                        it.data.elections
                    )
                }
                is Result.Error -> {
                    showError(Election.Upcoming)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getSavedElections()
    }

    private fun showLoading(election: Election) {
        when (election) {
            is Election.Upcoming -> binding.upcomingElectionsProgress.visibility = View.VISIBLE
            is Election.Saved -> binding.savedElectionsProgress.visibility = View.VISIBLE
        }
    }

    private fun showError(election: Election) {
        when (election) {
            is Election.Upcoming -> binding.upcomingElectionsProgress.visibility = View.INVISIBLE
            is Election.Saved -> binding.savedElectionsProgress.visibility = View.INVISIBLE
        }

        Toast.makeText(context, "Error loading data", Toast.LENGTH_SHORT).show()
    }

    sealed class Election {
        object Upcoming : Election()
        object Saved : Election()
    }
}