package com.paulcoding.tmdb.android.di

import com.paulcoding.tmdb.android.ui.viewmodels.MovieDetailViewModel
import com.paulcoding.tmdb.android.ui.viewmodels.MovieListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::MovieDetailViewModel)
    singleOf(::MovieListViewModel)
}