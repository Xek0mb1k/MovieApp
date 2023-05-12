package com.example.movieapp.data

import com.example.movieapp.domain.Movie
import com.example.movieapp.domain.SearchedMovieData
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("/")
    suspend fun getMovie(
        @Query("i") imdbID: String,
        @Query("apikey") key: String
    ): Movie

    @GET("/")
    suspend fun getSearchedMovieData(
        @Query("s") request: String,
        @Query("type") type: String,
        @Query("apikey") key: String

    ): SearchedMovieData

}