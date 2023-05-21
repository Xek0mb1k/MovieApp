package com.example.movieapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.R
import com.example.movieapp.domain.Movie

class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {

    private var movieList = listOf<Movie>()
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

        viewHolder.description.text = movieItem.Plot


    }

    class MovieItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.imageView)
        val title: TextView = view.findViewById(R.id.textView)
        val movieScore: TextView = view.findViewById(R.id.movieScoreTextView)

        val movieStar1: ImageView = view.findViewById(R.id.scoreStar1)
        val movieStar2: ImageView = view.findViewById(R.id.scoreStar2)
        val movieStar3: ImageView = view.findViewById(R.id.scoreStar3)
        val movieStar4: ImageView = view.findViewById(R.id.scoreStar4)
        val movieStar5: ImageView = view.findViewById(R.id.scoreStar5)

        val description: TextView = view.findViewById(R.id.descriptionTV)

    }
}