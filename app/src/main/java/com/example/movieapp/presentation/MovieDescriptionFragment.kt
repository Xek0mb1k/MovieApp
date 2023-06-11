package com.example.movieapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieDescriptionBinding
import com.example.movieapp.domain.Movie
import com.example.movieapp.domain.Search
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class MovieDescriptionFragment : Fragment() {

    private var _binding: FragmentMovieDescriptionBinding? = null
    private val binding get() = _binding!!

    private val vm by viewModel<MainViewModel>()

    private val sharedPref by lazy { activity?.getSharedPreferences("pref", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDescriptionBinding.inflate(inflater, container, false)

        vm.bookmarkMovieList = vm.getBookmarkList(sharedPref)

        binding.toBackButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val args = requireArguments()
        val searchData = args.getParcelable<Search>("ITEM_DATA_KEY") as Search

        enableLoadingScreen()

        loadAndSetMovieData(searchData)

        setDefaultBookmark(searchData)

        bookmarkClickHandler(searchData)

        return binding.root
    }

    private fun bookmarkClickHandler(searchData: Search) {
        binding.bookmarkButton.setOnClickListener {
            with(vm) {
                if (searchData in bookmarkMovieList) {
                    bookmarkMovieList.remove(searchData)
                    binding.bookmarkButton.setImageResource(R.drawable.bookmark_default)
                } else {
                    bookmarkMovieList.add(searchData)
                    binding.bookmarkButton.setImageResource(R.drawable.bookmark_active)
                }
            }
        }
    }

    private fun setDefaultBookmark(searchData: Search) {
        binding.bookmarkButton.setImageResource(
            if (searchData in vm.bookmarkMovieList) {
                R.drawable.bookmark_active
            } else {
                R.drawable.bookmark_default
            }
        )
    }

    private fun loadAndSetMovieData(searchData: Search) {
        CoroutineScope(Dispatchers.IO).launch {

            val movieItemData = vm.getMovie(searchData.imdbID)

            requireActivity().runOnUiThread {
                with(binding) {
                    disableLoadingScreen()

                    with(movieItemData) {
                        imageView.load(Poster)
                        movieNameTextView.text = Title
                        genreTV.text = Genre
                        descriptionTV.text = Plot
                        releaseValueTV.text = Released
                        runtimeValueTV.text = Runtime
                        directorValueTV.text = Director
                        actorsValueTV.text = Actors
                        awardsValueTV.text = Awards
                        setRaitingsScore(movieItemData)
                    }
                }
            }
        }
    }

    private fun FragmentMovieDescriptionBinding.disableLoadingScreen() {
        scrollView.visibility = View.VISIBLE
        bookmarkButton.visibility = View.VISIBLE
        toBackButton.visibility = View.VISIBLE

        progressBar.visibility = View.GONE
    }

    private fun enableLoadingScreen() {
        binding.scrollView.visibility = View.GONE
        binding.bookmarkButton.visibility = View.GONE
        binding.toBackButton.visibility = View.GONE

        binding.progressBar.visibility = View.VISIBLE

    }

    private fun FragmentMovieDescriptionBinding.setRaitingsScore(movieItemData: Movie) {
        if (movieItemData.Ratings.isEmpty()) {
            movieRatingTextView.text = "N/A"
        } else {
            movieRatingTextView.text = movieItemData.Ratings[0].Value

            val score = movieItemData.Ratings[0].Value.substring(
                0,
                movieItemData.Ratings[0].Value.indexOf("/")
            ).toDouble() / 2

            val integerScore = score.toInt()
            val remainingScore = score - integerScore

            for (i in integerScore + 1..5) {
                setEmptyStarImage(i)
            }

            if (remainingScore > 0) {
                setHalfStarImage(integerScore)
            }
        }
    }

    private fun FragmentMovieDescriptionBinding.setHalfStarImage(integerScore: Int) {
        when (integerScore + 1) {
            1 -> scoreStar1.setImageResource(R.drawable.half_star)
            2 -> scoreStar2.setImageResource(R.drawable.half_star)
            3 -> scoreStar3.setImageResource(R.drawable.half_star)
            4 -> scoreStar4.setImageResource(R.drawable.half_star)
            5 -> scoreStar5.setImageResource(R.drawable.half_star)
        }
    }

    private fun FragmentMovieDescriptionBinding.setEmptyStarImage(i: Int) {
        when (i) {
            1 -> scoreStar1.setImageResource(R.drawable.empty_star)
            2 -> scoreStar2.setImageResource(R.drawable.empty_star)
            3 -> scoreStar3.setImageResource(R.drawable.empty_star)
            4 -> scoreStar4.setImageResource(R.drawable.empty_star)
            5 -> scoreStar5.setImageResource(R.drawable.empty_star)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
        vm.saveBookmarkList(vm.bookmarkMovieList, sharedPref)
    }

}