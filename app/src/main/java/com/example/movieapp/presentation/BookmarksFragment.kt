package com.example.movieapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentBookmarksBinding
import com.example.movieapp.domain.Search
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        vm.bookmarkMovieList = vm.getBookmarkList(sharedPref)

        movieListAdapter.submitList(vm.bookmarkMovieList)

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
                        movieListAdapter.submitList(bookmarkMovieList)
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_default)
                    } else {
                        bookmarkMovieList.add(movieItem)
                        movieListAdapter.submitList(bookmarkMovieList)
                        viewHolder.bookmarkButton.setImageResource(R.drawable.bookmark_active)
                    }

                    saveBookmarkList(bookmarkMovieList, sharedPref)

                }

            }

    }
}
