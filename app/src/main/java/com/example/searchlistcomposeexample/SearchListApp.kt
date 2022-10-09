package com.example.searchlistcomposeexample

import android.app.Application
import com.example.testapp.utils.di.retrofitModule
import com.example.searchlistcomposeexample.utils.di.viewModelModule
import org.koin.core.context.startKoin

class SearchListApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(viewModelModule,retrofitModule))
        }
    }
}