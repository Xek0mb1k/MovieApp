package com.example.movieapp.presentation


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import coil.load
import com.example.movieapp.R
import com.example.movieapp.domain.Search


class MovieListAdapter : ListAdapter<Search, MovieItemViewHolder>(MovieItemDiffCallback()) {


    var onMovieItemClickListener: ((Search) -> Unit)? = null
    var onBookmarkButtonClickListener: ((movieItem: Search, viewHolder: MovieItemViewHolder) -> Unit)? =
        null
    var initMovieItem: ((Search, MovieItemViewHolder) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_card, parent, false
        )
        return MovieItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: MovieItemViewHolder, position: Int) {
        val movieItem = getItem(position)


        initMovieItem?.invoke(movieItem, viewHolder)


        viewHolder.poster.load(movieItem.Poster)

        viewHolder.itemView.setOnClickListener {
            onMovieItemClickListener?.invoke(movieItem)
        }

        viewHolder.bookmarkButton.setOnClickListener {
            onBookmarkButtonClickListener?.invoke(movieItem, viewHolder)
        }

        viewHolder.title.text = movieItem.Title
        viewHolder.movieYear.text = movieItem.Year
        viewHolder.type.text = movieItem.Type

    }




}
