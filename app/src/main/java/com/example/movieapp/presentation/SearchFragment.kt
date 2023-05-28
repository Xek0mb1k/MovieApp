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
import com.example.movieapp.domain.SearchedMovieData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val adapter: MovieListAdapter by lazy {
        MovieListAdapter()
    }
    private val vm by viewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchView.isActivated = true
        binding.searchView.queryHint = getString(R.string.search_hint)
        binding.searchView.onActionViewExpanded()
        binding.searchView.isIconified = false
        binding.searchView.clearFocus()

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
                        while (response) {
                            val movies = vm.getSearchedMovie(query, "", page++)
                            response = movies.Response == "True"
                            if (response) {
                                searchedMovie = movies
                                vm.movieList.addAll(searchedMovie.Search)
                                isEmptyList = false
                            }

                        }

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

                                adapter.movieList = vm.movieList
                            }
                        }
                    }

                }else{
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.movieItemsSpinnerRecyclerView.adapter = adapter
    }
}