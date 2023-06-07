package com.example.movieapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.domain.Search
import com.example.movieapp.domain.SearchedMovieData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val movieListAdapter by lazy {
        MovieListAdapter()
    }
    private val vm by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        with(binding.searchView) {
            isActivated = true
            queryHint = getString(R.string.search_hint)
            onActionViewExpanded()
            isIconified = false
            clearFocus()
        }


        setupRecyclerView()

        var isSearchViewSubmitted = false

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                if (!isSearchViewSubmitted) {
                    isSearchViewSubmitted = true

                    Log.d("DEBUG", "QUERY $query")
                    binding.progressBar.visibility = View.VISIBLE
                    binding.movieItemsSpinnerRecyclerView.visibility = View.GONE
                    vm.movieList.clear()
                    CoroutineScope(Dispatchers.IO).launch {
                        var response = true
                        var page = 1
                        var isEmptyList = true
                        lateinit var searchedMovie: SearchedMovieData
//                        while (response) { TODO("FIX THIS")
                        val movies = vm.getSearchedMovie(query, "", page++)
                        response = movies.Response == "True"
                        if (response) {
                            searchedMovie = movies
                            vm.movieList.addAll(searchedMovie.Search)
                            isEmptyList = false
                        }

//                        }

                        activity!!.runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            binding.movieItemsSpinnerRecyclerView.visibility = View.VISIBLE
                            if (isEmptyList) {
                                Toast.makeText(
                                    activity, "Movie not found! Please try again!", LENGTH_SHORT
                                ).show()
                                vm.movieList.clear()
                            } else {
                                val format = resources.getString(R.string.search_results)
                                binding.searchTextView.text =
                                    String.format(format, searchedMovie.totalResults)

                                movieListAdapter.movieList = vm.movieList
                            }
                        }
                    }

                } else {
                    isSearchViewSubmitted = false
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })



        return binding.root
    }

    override fun onPause() {
        // TODO("IMPLEMENTED SAVE DATA")
        Log.d("DEBUG", "Pause VIEW")

        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        val rvMovieList = binding.movieItemsSpinnerRecyclerView
        with(rvMovieList) {
            adapter = movieListAdapter
        }
        binding.movieItemsSpinnerRecyclerView.adapter = movieListAdapter
        movieListAdapter.initMovieItem = {movieItem: Search, viewHolder: MovieListAdapter.MovieItemViewHolder ->
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
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
                    } else {
                        bookmarkMovieList.add(movieItem)
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
                    }

                    // DEBUG
                    for (i in bookmarkMovieList) {
                        Log.d("DEBUG", "SEARCH FRAGMENT: " + i.Title)
                    }

                }

            }

    }
}