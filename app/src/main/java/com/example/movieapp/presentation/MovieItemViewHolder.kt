package com.example.movieapp.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R

class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val poster: ImageView = view.findViewById(R.id.posterImageView)
    val bookmarkButton: ImageView = view.findViewById(R.id.bookmarkButton)
    val title: TextView = view.findViewById(R.id.titleTextView)
    val movieYear: TextView = view.findViewById(R.id.movieRatingTextView)

    val type: TextView = view.findViewById(R.id.movieTypeTextView)
}