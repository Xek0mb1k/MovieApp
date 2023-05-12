package com.example.movieapp.domain

class GetSearchedMovieDataUseCase(private val movieRepository: MovieRepository) {
    suspend fun getSearchedMovieData(request: String, type: String): SearchedMovieData {
        return movieRepository.getSearchedMovieData(request, type)
    }
}