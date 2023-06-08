package com.example.movieapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentBookmarksBinding
import com.example.movieapp.domain.Search
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<MainViewModel>()

    private val movieListAdapter by lazy {
        MovieListAdapter()
    }

    private val sharedPref by lazy { activity?.getSharedPreferences("pref", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)


        setupRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("DEBUG", sharedPref.toString())
        vm.bookmarkMovieList = vm.getBookmarkList(sharedPref)

        // DEBUG
        Log.d("DEBUG", "BOOKMARK FRAGMENT: " + vm.bookmarkMovieList)
        for(i in vm.bookmarkMovieList) {
            Log.d("DEBUG", "BOOKMARK FRAGMENT: " + i.Title)
        }

        movieListAdapter.movieList = vm.bookmarkMovieList

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        vm.saveBookmarkList(vm.bookmarkMovieList, sharedPref)
    }


    private fun setupRecyclerView() {

        val rvMovieList = binding.bookmarkedMovieItemsSpinner
        with(rvMovieList) {
            adapter = movieListAdapter
        }
        binding.bookmarkedMovieItemsSpinner.adapter = movieListAdapter
        movieListAdapter.initMovieItem =
            { movieItem: Search, viewHolder: MovieListAdapter.MovieItemViewHolder ->
                viewHolder.bookmarkButton.setImageResource(
                    if (movieItem in vm.bookmarkMovieList) {
                        R.drawable.bookmark_active
                    } else {
                        R.drawable.bookmark_default
                    }
                )
            }

        movieListAdapter.onMovieItemClickListener = {
            Log.d("DEBUG", it.Title + " " + it.imdbID)
        }

        movieListAdapter.onBookmarkButtonClickListener =
            { movieItem: Search, viewHolder: MovieListAdapter.MovieItemViewHolder ->

                with(vm) {
                    if (movieItem in bookmarkMovieList) {
                        bookmarkMovieList.remove(movieItem)
                        viewHolder.itemView.visibility = GONE
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
                    } else {
                        bookmarkMovieList.add(movieItem)
                        viewHolder.itemView.visibility = VISIBLE
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
                    }

                    // DEBUG
                    for (i in bookmarkMovieList) {
                        Log.d("DEBUG", "SEARCH FRAGMENT: " + i.Title)
                    }

                    saveBookmarkList(bookmarkMovieList, sharedPref)

                }

            }

    }
}
