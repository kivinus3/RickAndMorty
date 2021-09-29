package com.kivinus.rickandmorty.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.kivinus.rickandmorty.utils.CharacterInfoResponse
import com.kivinus.rickandmorty.R
import com.kivinus.rickandmorty.databinding.FragmentCharacterDetailBinding
import com.kivinus.rickandmorty.model.CharacterDetailInfo
import com.kivinus.rickandmorty.viewmodels.CharacterDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {

    private val args: CharacterDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CharacterDetailViewModel>()
    private val binding by viewBinding(FragmentCharacterDetailBinding::bind)
    private val navController: NavController
            by lazy { findNavController() }

    private fun setupUI() {
        binding.fab.setOnClickListener { navController.navigateUp() }

        binding.btnRetry.setOnClickListener {
            viewModel.fetchCharacterByID(args.characterID)
                .also { showLoading() }
        }
    }

    private fun showLoading() {
        binding.apply {
            tvError.visibility = View.INVISIBLE
            btnRetry.visibility = View.INVISIBLE
            loadingView.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        binding.apply {
            tvError.visibility = View.VISIBLE
            loadingView.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
            btnRetry.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            tvError.visibility = View.INVISIBLE
            loadingView.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            btnRetry.visibility = View.INVISIBLE
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupCharacterInfo(character: CharacterDetailInfo) {
        binding.apply {
            Glide.with(root)
                .load(character.image)
                .into(binding.ivPortrait)
            val indicatorRes = when (character.status) {
                "Alive" -> R.drawable.circle_indicator_green
                "Dead" -> R.drawable.circle_indicator_red
                else -> R.drawable.circle_indicator_gray
            }
            indicator.setBackgroundResource(indicatorRes)
            tvName.text = character.name
            tvStatusAndRace.text = "${character.status.replaceFirstChar(Char::uppercase)} " +
                    "- ${character.species}"
            var info = ""
            info += character.gender.lowercase().replaceFirstChar(Char::uppercase)
            info += "\n \n"
            info += character.origin.lowercase().replaceFirstChar(Char::uppercase)
            info += "\n \n"
            info += character.locationName.lowercase().replaceFirstChar(Char::uppercase)
            info += "\n \n"
            info += character.locationType.lowercase().replaceFirstChar(Char::uppercase)
            info += "\n \n"
            info += character.locationDimension.lowercase().replaceFirstChar(Char::uppercase)
            info += "\n \n"
            info += character.episodes.toString()
            info += "\n \n"
            tvInfo.text = info
        }
    }

    private fun setupJob() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.response.filterNotNull()
                    .collectLatest {
                        when (it) {
                            is CharacterInfoResponse.Success ->
                                setupCharacterInfo(it.character).also { hideLoading() }
                            is CharacterInfoResponse.Error ->
                                showError()
                            else -> showLoading()
                        }
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchCharacterByID(args.characterID)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupJob()
    }

}