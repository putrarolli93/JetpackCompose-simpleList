package com.example.searchlistcomposeexample.utils.di

import com.example.searchlistcomposeexample.repository.MainRepository
import com.example.searchlistcomposeexample.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(get())
    }

    single {
        MainRepository(get())
    }

}