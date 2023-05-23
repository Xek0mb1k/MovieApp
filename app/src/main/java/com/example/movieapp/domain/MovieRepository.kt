package com.example.movieapp.domain

interface MovieRepository {
    suspend fun getSearchedMovieData(request: String, type: String, page: Int): SearchedMovieData
    suspend fun getMovie(imdbID: String): Movie

}