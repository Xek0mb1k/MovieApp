package com.example.movieapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.movieapp.domain.Search

class MovieItemDiffCallback: DiffUtil.ItemCallback<Search>() {
    override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem.imdbID == newItem.imdbID
    }

    override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Search, newItem: Search): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}