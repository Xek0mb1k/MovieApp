package com.example.movieapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.R
import com.example.movieapp.domain.Search

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {

    var movieList = listOf<Search>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_card,
            parent,
            false
        )
        return MovieItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(viewHolder: MovieItemViewHolder, position: Int) {
        val movieItem = movieList[position]

        viewHolder.poster.load(movieItem.Poster)
        viewHolder.title.text = movieItem.Title

        viewHolder.movieYear.text = movieItem.Year

        viewHolder.type.text = movieItem.Type

    }


    class MovieItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.posterImageView)
        val title: TextView = view.findViewById(R.id.titleTextView)
        val movieYear: TextView = view.findViewById(R.id.movieYearTextView)

        val type: TextView = view.findViewById(R.id.movieTypeTextView)

    }
}