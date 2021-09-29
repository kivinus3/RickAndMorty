package com.kivinus.rickandmorty.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kivinus.rickandmorty.R
import com.kivinus.rickandmorty.adapters.RamCharactersListAdapter
import com.kivinus.rickandmorty.adapters.RamLoadStateAdapter
import com.kivinus.rickandmorty.api.response.RamApiCharacterResponse
import com.kivinus.rickandmorty.databinding.FragmentCharactersListBinding
import com.kivinus.rickandmorty.viewmodels.CharactersListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersListFragment : Fragment(R.layout.fragment_characters_list),
    RamCharactersListAdapter.OnItemClickListener {

    private val viewModel by viewModels<CharactersListViewModel>()
    private val binding by viewBinding(FragmentCharactersListBinding::bind)
    private val rvAdapter: RamCharactersListAdapter
            by lazy { RamCharactersListAdapter(this) }
    private val navController: NavController
            by lazy { findNavController() }

    // click listener for rv items
    override fun onItemClick(apiCharacter: RamApiCharacterResponse) {
        val action = CharactersListFragmentDirections
            .navigateCharactersListFragmentToCharacterDetailFragment(apiCharacter.id)
        navController.navigate(action)
    }

    private fun setupUI() {

        binding.apply {
            recyclerViewCharacters.adapter = rvAdapter.withLoadStateHeaderAndFooter(
                RamLoadStateAdapter { rvAdapter.retry() },
                RamLoadStateAdapter { rvAdapter.retry() }
            )
            recyclerViewCharacters.itemAnimator = null
            btnRetry.setOnClickListener { rvAdapter.retry() }
        }

        // load state listener
        rvAdapter.addLoadStateListener { state ->
            binding.apply {
                progressBar.isVisible = state.source.refresh is LoadState.Loading

                recyclerViewCharacters.isVisible = state.source.refresh is LoadState.NotLoading

                btnRetry.isVisible = state.source.refresh is LoadState.Error
            }
        }
    }

    private fun setupJob() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.characters.filterNotNull()
                    .collectLatest { characters -> rvAdapter.submitData(characters) }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupJob()
    }

}