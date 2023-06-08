package com.example.movieapp.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainViewModel(repository: MovieRepository): ViewModel() {

    val movieList = mutableListOf<Search>()


    var bookmarkMovieList = mutableListOf<Search>()

    private val getMovieUseCase = GetMovieUseCase(repository)
    private val getSearchedMovieDataUseCase = GetSearchedMovieDataUseCase(repository)

    suspend fun getMovie(imdbID: String): Movie{
        return getMovieUseCase.getMovie(imdbID)
    }

    suspend fun getSearchedMovie(request: String, type: String, page: Int): SearchedMovieData{
        return getSearchedMovieDataUseCase.getSearchedMovieData(request, type, page)
    }

    fun saveBookmarkList(bookmarkList: MutableList<Search>, sharedPreferences: SharedPreferences?){
        val editor = sharedPreferences?.edit()
        editor?.putString(bookmarkListKey, Gson().toJson(bookmarkList))
        editor?.apply()
    }

    fun getBookmarkList(sharedPreferences: SharedPreferences?): MutableList<Search>{
        val bookmarkListJson = sharedPreferences?.getString(bookmarkListKey, null)

        val bookmarkMovieList = if (bookmarkListJson != null) {

            Gson().fromJson<MutableList<Search>>(
                bookmarkListJson, object : TypeToken<MutableList<Search>>() {}.type
            ).toMutableList()
        } else {

            mutableListOf()
        }

        return bookmarkMovieList
    }

    companion object{
        private const val bookmarkListKey = "BOOKMARK_LIST"
    }
}

