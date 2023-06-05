package com.example.movieapp.di

import com.example.movieapp.data.MovieRepositoryImpl
import com.example.movieapp.domain.MovieRepository
import com.example.movieapp.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single<MovieRepository> { MovieRepositoryImpl() }

    viewModel { MainViewModel(get()) }
}
