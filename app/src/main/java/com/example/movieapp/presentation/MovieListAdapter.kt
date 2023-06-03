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
                val searchListType = object : TypeToken<MutableList<Search>>() {}.type
                gson.fromJson(bookmarkListJson, searchListType)
            } else {
                mutableListOf<Search>()
            }

            if (movieItem in bookmarkMovieList) {
                bookmarkMovieList.remove(movieItem)

                sharedPref.edit().putString("bookmarkList", gson.toJson(bookmarkMovieList))
                sharedPref.edit().apply()
                viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
            } else {
                bookmarkMovieList.add(movieItem)
                sharedPref.edit().putString("bookmarkList", gson.toJson(bookmarkMovieList))
                sharedPref.edit().apply()
                viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
            }

            Log.d("DEBUG", sharedPref.getString("bookmarkList", "").toString())
            val searchListType = object : TypeToken<MutableList<Search>>() {}.type
            if (sharedPref.getString("bookmarkList", null) != null) {
                Log.d(
                    "DEBUG",
                    gson.fromJson(sharedPref.getString("bookmarkList", null), searchListType)
                )
            }

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
