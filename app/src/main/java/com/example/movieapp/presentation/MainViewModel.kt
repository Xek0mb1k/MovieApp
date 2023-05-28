package com.example.movieapp.presentation

import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.*

class MainViewModel(repository: MovieRepository): ViewModel() {

    val movieList = mutableListOf<Search>()
    val bookmarkMovieList = mutableListOf<Search>()

    private val getMovieUseCase = GetMovieUseCase(repository)
    private val getSearchedMovieDataUseCase = GetSearchedMovieDataUseCase(repository)

    suspend fun getMovie(imdbID: String): Movie{
        return getMovieUseCase.getMovie(imdbID)
    }

    suspend fun getSearchedMovie(request: String, type: String, page: Int): SearchedMovieData{
        return getSearchedMovieDataUseCase.getSearchedMovieData(request, type, page)
    }
}
