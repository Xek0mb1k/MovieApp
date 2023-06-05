package com.example.movieapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.example.movieapp.databinding.FragmentBookmarksBinding
import com.example.movieapp.domain.Search
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel


class BookmarksFragment : Fragment() {



    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<MainViewModel>()

    private val adapter: MovieListAdapter by lazy {
        activity?.applicationContext?.let { MovieListAdapter(it) }!!
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
        val sharedPref =
            activity?.let { PreferenceManager.getDefaultSharedPreferences(it) }

        val bookmarkListJson = sharedPref?.getString("bookmarkList", null)
        val gson = Gson()

        val bookmarkMovieList = if (bookmarkListJson != null) {
            binding.errorMessageTextView.visibility = GONE
            gson.fromJson<MutableList<Search>>(
                bookmarkListJson, object : TypeToken<MutableList<Search>>() {}.type
            ).toMutableList()
        } else {
            binding.errorMessageTextView.visibility = VISIBLE
            mutableListOf<Search>()
        }

        adapter.movieList = bookmarkMovieList

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.bookmarkedMovieItemsSpinner.adapter = adapter
    }
}
