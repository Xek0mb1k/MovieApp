package com.example.movieapp.data

import com.example.movieapp.domain.Movie
import com.example.movieapp.domain.MovieRepository
import com.example.movieapp.domain.SearchedMovieData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieRepositoryImpl: MovieRepository {

    private val apiKey = "7c296a04"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://www.omdbapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val movieApi: MovieApi = retrofit.create(MovieApi::class.java)


    override suspend fun getSearchedMovieData(request: String, type: String, page: Int): SearchedMovieData {
        return movieApi.getSearchedMovieData(request, type, page, apiKey)
    }

    override suspend fun getMovie(imdbID: String): Movie {
        return movieApi.getMovie(imdbID, apiKey)
    }

}