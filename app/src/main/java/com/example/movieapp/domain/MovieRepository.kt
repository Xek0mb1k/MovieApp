package com.example.movieapp.domain

interface MovieRepository {
    fun getSearchedMovieData(request: String): SearchedMovieData
    fun getMovie(imdbID: String): Movie

}