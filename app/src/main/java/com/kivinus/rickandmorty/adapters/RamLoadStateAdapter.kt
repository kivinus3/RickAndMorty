package com.kivinus.rickandmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kivinus.rickandmorty.databinding.LoadStateFooterBinding


class RamLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RamLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetryFooter.setOnClickListener { retry.invoke() }
        }

        fun bind(state: LoadState) {
            binding.apply {
                progressBarFooter.isVisible = state is LoadState.Loading
                btnRetryFooter.isVisible = state !is LoadState.Loading
                textViewErrorFooter.isVisible = state !is LoadState.Loading
            }
        }
    }
}