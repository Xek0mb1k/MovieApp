package com.example.movieapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieDescriptionBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDescriptionBinding.inflate(inflater, container, false)

        binding.toBackButton.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val args = requireArguments()
        val searchData  = args.getParcelable<Search>("ITEM_DATA_KEY") as Search



        CoroutineScope(Dispatchers.IO).launch {

            val movieItemData = vm.getMovie(searchData.imdbID)

            requireActivity().runOnUiThread {
                with(binding){
                    imageView.load(movieItemData.Poster)
                    movieNameTextView.text = movieItemData.Title

                    with(movieItemData){
                        descriptionTV.text = Plot
                        releaseValueTV.text = Released
                        runtimeValueTV.text = Runtime
                        directorValueTV.text = Director
                        actorsValueTV.text = Actors
                        awardsValueTV.text = Awards

                        movieRatingTextView.text = movieItemData.Rated
                        scoreStar1
                        scoreStar2
                        scoreStar3
                        scoreStar4
                        scoreStar5
                    }

                }
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE
    }

}