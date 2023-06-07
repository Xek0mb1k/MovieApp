package com.example.movieapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.databinding.FragmentBookmarksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<MainViewModel>()

    private val adapter by lazy {
        MovieListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()

        // DEBUG
        Log.d("DEBUG", "BOOKMARK FRAGMENT: " + vm.bookmarkMovieList)
        for(i in vm.bookmarkMovieList) {
            Log.d("DEBUG", "BOOKMARK FRAGMENT: " + i.Title)
        }
        adapter.movieList = vm.bookmarkMovieList

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.bookmarkedMovieItemsSpinner.adapter = adapter
    }
}
