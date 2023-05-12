package com.example.movieapp.domain

class GetMovieUseCase(private val movieRepository: MovieRepository) {
    fun getMovie(imdbID: String): Movie{
        return movieRepository.getMovie(imdbID)
    }
}