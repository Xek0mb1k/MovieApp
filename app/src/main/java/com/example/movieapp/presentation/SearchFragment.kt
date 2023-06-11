package com.example.movieapp.presentation

import android.content.Context
import android.os.Bundle
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
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.max


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val movieListAdapter by lazy {
        MovieListAdapter()
    }
    private val vm by viewModel<MainViewModel>()

    private val sharedPref by lazy { activity?.getSharedPreferences("pref", Context.MODE_PRIVATE) }

    private var page = FIRST_PAGE

    private var query = ""

    private var maxPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        initSearchView()

        vm.bookmarkMovieList = vm.getBookmarkList(sharedPref)

        setupRecyclerView()

        var isSearchViewSubmitted = false

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(localQuery: String): Boolean {
                if (!isSearchViewSubmitted) {
                    maxPosition = 0
                    isSearchViewSubmitted = true

                    setupRecyclerView()

                    initSearch(localQuery)

                    loadPage()

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

    private fun initSearch(localQuery: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.movieItemsSpinnerRecyclerView.visibility = View.GONE
        vm.movieList.clear()
        page = FIRST_PAGE
        query = localQuery
        movieListAdapter.submitList(vm.movieList)
    }

    private fun loadPage() {

        lateinit var searchedMovie: SearchedMovieData
        CoroutineScope(Dispatchers.IO).launch {

            val movies = vm.getSearchedMovie(query, "", page)

            requireActivity().runOnUiThread {

                val response = movies.Response == "True"
                if (response) {
                    searchedMovie = movies
                    vm.movieList.addAll(searchedMovie.Search)
                }

                binding.progressBar.visibility = View.GONE
                binding.movieItemsSpinnerRecyclerView.visibility = View.VISIBLE
                if (!response) {
                    Toast.makeText(
                        activity, "Movie not found!", LENGTH_SHORT
                    ).show()
                    vm.movieList.clear()
                    val format = resources.getString(R.string.search_results)
                    binding.searchTextView.text =
                        String.format(format, 0)
                } else {
                    val format = resources.getString(R.string.search_results)
                    binding.searchTextView.text =
                        String.format(format, searchedMovie.totalResults)

                    movieListAdapter.submitList(vm.movieList)
                }
            }
        }
    }

    private fun initSearchView() {
        with(binding.searchView) {
            isActivated = true
            queryHint = getString(R.string.search_hint)
            onActionViewExpanded()
            isIconified = false
            clearFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        vm.saveBookmarkList(vm.bookmarkMovieList, sharedPref)
    }

    private fun setupRecyclerView() {

        val rvMovieList = binding.movieItemsSpinnerRecyclerView
        with(rvMovieList) {
            adapter = movieListAdapter
        }
        binding.movieItemsSpinnerRecyclerView.adapter = movieListAdapter
        movieListAdapter.initMovieItem =
            { movieItem: Search, viewHolder: MovieItemViewHolder ->
                viewHolder.bookmarkButton.setImageResource(
                    if (movieItem in vm.bookmarkMovieList) {
                        R.drawable.bookmark_active
                    } else {
                        R.drawable.bookmark_default
                    }
                )
            }

        movieListAdapter.onMovieItemClickListener = {


            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(
                R.id.fragment,
                MovieDescriptionFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("ITEM_DATA_KEY", it)
                    }
                }
            )
            transaction?.addToBackStack("MovieDescriptionFragment")
            transaction?.commit()
            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
                View.GONE


        }

        movieListAdapter.onBookmarkButtonClickListener =
            { movieItem: Search, viewHolder: MovieItemViewHolder ->

                with(vm) {
                    if (movieItem in bookmarkMovieList) {
                        bookmarkMovieList.remove(movieItem)
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
                    } else {
                        bookmarkMovieList.add(movieItem)
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
                    }
                }

            }

        movieListAdapter.loadNextPage =
            {
                if (it % 8 == 0 && maxPosition < it) {
                    page++
                    loadPage()
                }
                maxPosition = max(maxPosition, it)


            }
    }


    companion object {
        private const val FIRST_PAGE = 1
    }
}