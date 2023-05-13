package com.example.movieapp.presentation

import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.*

class MainViewModel(repository: MovieRepository): ViewModel() {
    private val getMovieUseCase = GetMovieUseCase(repository)
    private val getSearchedMovieDataUseCase = GetSearchedMovieDataUseCase(repository)

    suspend fun getMovie(imdbID: String): Movie{
        return getMovieUseCase.getMovie(imdbID)
    }

    suspend fun getSearchedMovie(request: String, type: String): SearchedMovieData{
        return getSearchedMovieDataUseCase.getSearchedMovieData(request, type)
    }
}
