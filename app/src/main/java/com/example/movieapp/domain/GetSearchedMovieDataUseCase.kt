package com.example.movieapp.domain

class GetSearchedMovieDataUseCase(private val movieRepository: MovieRepository) {
    fun getSearchedMovieData(request: String): SearchedMovieData {
        return movieRepository.getSearchedMovieData(request)
    }
}