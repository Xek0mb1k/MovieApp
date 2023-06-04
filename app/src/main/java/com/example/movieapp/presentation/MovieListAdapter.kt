package com.example.movieapp.presentation


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.R
import com.example.movieapp.domain.Search
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MovieListAdapter : RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {
    var movieList = listOf<Search>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_card, parent, false
        )
        return MovieItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("CommitPrefEdits")
    override fun onBindViewHolder(viewHolder: MovieItemViewHolder, position: Int) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(viewHolder.itemView.context)

        val movieItem = movieList[position]

        viewHolder.poster.load(movieItem.Poster)
        viewHolder.bookmarkButton.setOnClickListener {


            val bookmarkListJson = sharedPref.getString("bookmarkList", null)
            val gson = Gson()
            val bookmarkMovieList = if (bookmarkListJson != null) {
                gson.fromJson<MutableList<Search>>(
                    bookmarkListJson,
                    object : TypeToken<MutableList<Search>>() {}.type
                )
                    .toMutableList()
            } else {
                mutableListOf<Search>()
            }

            if (movieItem in bookmarkMovieList) {
                bookmarkMovieList.remove(movieItem)
                viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
            } else {
                bookmarkMovieList.add(movieItem)
                viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
            }
            sharedPref.edit().putString("bookmarkList", gson.toJson(bookmarkMovieList)).apply()

            Log.d(
                "DEBUG", "JSON  " +
                gson.toJson(bookmarkMovieList)
            )

            val searchList =
                gson.fromJson<MutableList<Search>>(
                    gson.toJson(bookmarkMovieList),
                    object : TypeToken<MutableList<Search>>() {}.type
                )
                    .toMutableList()
            Log.d(
                "DEBUG", "MutableList  " +
                        searchList.toString()
            )

            Log.d("DEBUG", sharedPref.getString("bookmarkList", null).toString())

        }
        viewHolder.title.text = movieItem.Title

        viewHolder.movieYear.text = movieItem.Year

        viewHolder.type.text = movieItem.Type

    }


    class MovieItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.posterImageView)
        val bookmarkButton: ImageView = view.findViewById(R.id.bookmarkButton)
        val title: TextView = view.findViewById(R.id.titleTextView)
        val movieYear: TextView = view.findViewById(R.id.movieYearTextView)

        val type: TextView = view.findViewById(R.id.movieTypeTextView)

    }
}
