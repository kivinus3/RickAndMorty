package com.kivinus.rickandmorty.adapters

import android.annotation.SuppressLint
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kivinus.rickandmorty.R
import com.kivinus.rickandmorty.api.response.RamApiCharacterResponse
import com.kivinus.rickandmorty.databinding.ListItemCharacterBinding

class RamCharactersListAdapter(private val clickListener: OnItemClickListener) :
    PagingDataAdapter<RamApiCharacterResponse, RamCharactersListAdapter.CharactersViewHolder>
        (CharactersDiffCallBack) {

    // ADAPTER
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder =
        CharactersViewHolder(
            ListItemCharacterBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) =
        holder.bind(getItem(position)!!)


    interface OnItemClickListener {
        fun onItemClick(apiCharacter: RamApiCharacterResponse)
    }

    //HOLDER
    inner class CharactersViewHolder(
        private val binding: ListItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val item = getItem(bindingAdapterPosition)
                    if (item != null)
                        clickListener.onItemClick((item))
                }
            }
        }


        @SuppressLint("SetTextI18n")
        fun bind(apiCharacter: RamApiCharacterResponse) {
            binding.characterPortrait.outlineProvider = object : ViewOutlineProvider() {

                override fun getOutline(view: View?, outline: Outline?) {
                    outline?.setRoundRect(
                        0,
                        0,
                        (view!!.height + 40F).toInt(),
                        view.width,
                        40F
                    )
                }
            }

            binding.characterPortrait.clipToOutline = true
            Glide.with(binding.root)
                .load(apiCharacter.image)
                .into(binding.characterPortrait)
            binding.apply {
                val indicatorRes = when (apiCharacter.status) {
                    "Alive" -> R.drawable.circle_indicator_green
                    "Dead" -> R.drawable.circle_indicator_red
                    else -> R.drawable.circle_indicator_gray
                }
                indicator.setBackgroundResource(indicatorRes)
                tvName.text = apiCharacter.name
                tvStatusAndRace.text =
                    "${apiCharacter.status.replaceFirstChar(Char::uppercase)} - ${apiCharacter.species}"
                val firstEpisodeUrl = apiCharacter.episode[0]
                val firstEpisode = firstEpisodeUrl.substringAfterLast('/')
                tvFirstSeenInput.text = "Episode $firstEpisode"
                tvLocationInput.text = apiCharacter.location.name
            }
        }
    }

    // DIFF_UTIL
    companion object {
        val CharactersDiffCallBack = object : DiffUtil.ItemCallback<RamApiCharacterResponse>() {
            override fun areItemsTheSame(
                oldItem: RamApiCharacterResponse,
                newItem: RamApiCharacterResponse
            ): Boolean = oldItem.id == newItem.id


            override fun areContentsTheSame(
                oldItem: RamApiCharacterResponse,
                newItem: RamApiCharacterResponse
            ): Boolean = oldItem == newItem

        }
    }
}