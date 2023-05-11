package com.example.movieapp.domain

data class SearchedMovieData(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)