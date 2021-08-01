package com.example.android.politicalpreparedness.ui_screens.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.Result
import com.example.android.politicalpreparedness.databinding.FragmentElectionInformationBinding

class ElectionInfoFragment : Fragment() {
    //TODO: Declare ViewModel
    private val viewModel: ElectionInfoViewModel by lazy {
        val viewModelFactory = ElectionInfoViewModelFactory(requireContext())
        ViewModelProvider(this, viewModelFactory).get(ElectionInfoViewModel::class.java)
    }

    private val args: ElectionInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getElectionStatus(args.election)
        viewModel.getElectionInfo(args.election)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentElectionInformationBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel
        viewModel.election = args.election

        //TODO: Add binding values
        binding.viewModel = viewModel

        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        viewModel.buttonState.observe(viewLifecycleOwner) {
            when (it) {
                is ButtonState.Followed -> {
                    binding.button.text = getString(R.string.button_unfollow_election)
                }

                is ButtonState.UnFollowed -> {
                    binding.button.text = getString(R.string.button_follow_election)
                }
            }
        }

        //TODO: Handle loading of URLs
        binding.votingLocations.setOnClickListener {
            val result = viewModel.electionInfo.value
            if (result is Result.Success) {
                val voterInfoResponse = result.data
                if (!voterInfoResponse.state.isNullOrEmpty()) {
                    val votingLocationUrl =
                        voterInfoResponse.state[0].electionAdministrationBody.votingLocationFinderUrl

                    if (!votingLocationUrl.isNullOrBlank()) {
                        setIntent(votingLocationUrl)
                    }
                }
            } else {
                showError()
            }
        }

        binding.ballotInformation.setOnClickListener {
            val result = viewModel.electionInfo.value
            if (result is Result.Success) {
                val voterInfoResponse = result.data
                if (!voterInfoResponse.state.isNullOrEmpty()) {
                    val ballotInfoUrl =
                        voterInfoResponse.state[0].electionAdministrationBody.ballotInfoUrl

                    if (!ballotInfoUrl.isNullOrBlank()) {
                        setIntent(ballotInfoUrl)
                    }
                }
            } else {
                showError()
            }
        }

        //TODO: Handle save button UI state
        binding.button.setOnClickListener {
            when (viewModel.buttonState.value) {
                is ButtonState.Followed -> {
                    viewModel.unfollowElection(args.election)
                }

                is ButtonState.UnFollowed -> {
                    viewModel.followElection(args.election)
                }
            }
        }
        //TODO: cont'd Handle save button clicks

        return binding.root
    }

    //TODO: Create method to load URL intents
    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun showError() {
        Toast.makeText(context, getString(R.string.error_loading_data), Toast.LENGTH_SHORT).show()
    }
}