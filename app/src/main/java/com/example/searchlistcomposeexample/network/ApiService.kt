package com.example.searchlistcomposeexample.network

import com.example.searchlistcomposeexample.viewmodel.model.ListData
import retrofit2.http.GET

interface ApiService {

    @GET("posts")
    suspend fun getData(): MutableList<ListData>

}