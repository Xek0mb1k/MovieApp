package com.example.movieapp.presentation


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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


class MovieListAdapter(private val context: Context) :
    RecyclerView.Adapter<MovieListAdapter.MovieItemViewHolder>() {
    var movieList = listOf<Search>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onMovieItemClickListener: OnMovieItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_card, parent, false
        )
        return MovieItemViewHolder(view)
    }


    @SuppressLint("CommitPrefEdits")
    override fun onBindViewHolder(viewHolder: MovieItemViewHolder, position: Int) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context)

        val bookmarkMovieList = getMovieList(sharedPref)

        val movieItem = movieList[position]

        setBookmarkButtonIcon(viewHolder, movieItem, bookmarkMovieList)

        viewHolder.poster.load(movieItem.Poster)

        viewHolder.bookmarkButton.setOnClickListener {
            changeBookmarkIcon(movieItem, bookmarkMovieList, viewHolder)
            putBookmarkMovieListInSharedPref(sharedPref, bookmarkMovieList)
        }
        viewHolder.title.text = movieItem.Title

        viewHolder.movieYear.text = movieItem.Year

        viewHolder.type.text = movieItem.Type

    }

    private fun changeBookmarkIcon(
        movieItem: Search,
        bookmarkMovieList: MutableList<Search>,
        viewHolder: MovieItemViewHolder
    ) {
        if (movieItem in bookmarkMovieList) {
            bookmarkMovieList.remove(movieItem)
            viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
        } else {
            bookmarkMovieList.add(movieItem)
            viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
        }
    }

    private fun setBookmarkButtonIcon(
        viewHolder: MovieItemViewHolder,
        movieItem: Search,
        bookmarkMovieList: MutableList<Search>
    ) {
        viewHolder.bookmarkButton.setImageResource(
            if (movieItem in bookmarkMovieList) {
                R.drawable.bookmark_active
            } else {
                R.drawable.bookmark_default
            }
        )
    }

    private fun putBookmarkMovieListInSharedPref(
        sharedPref: SharedPreferences,
        bookmarkMovieList: MutableList<Search>
    ) {
        val gson = Gson()
        sharedPref.edit().putString("bookmarkList", gson.toJson(bookmarkMovieList)).apply()
    }

    private fun getMovieList(sharedPref: SharedPreferences): MutableList<Search> {
        val bookmarkListJson = sharedPref.getString("bookmarkList", null)

        val gson = Gson()

        return if (bookmarkListJson != null) {
            gson.fromJson<MutableList<Search>>(
                bookmarkListJson,
                object : TypeToken<MutableList<Search>>() {}.type
            )
                .toMutableList()
        } else {
            mutableListOf()
        }
    }


    override fun getItemCount(): Int {
        return movieList.size
    }


    class MovieItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.posterImageView)
        val bookmarkButton: ImageView = view.findViewById(R.id.bookmarkButton)
        val title: TextView = view.findViewById(R.id.titleTextView)
        val movieYear: TextView = view.findViewById(R.id.movieYearTextView)

        val type: TextView = view.findViewById(R.id.movieTypeTextView)
    }

    interface OnMovieItemClickListener {
        fun onMovieItemClickListener(movieItem: List<Search>)
    }
}
