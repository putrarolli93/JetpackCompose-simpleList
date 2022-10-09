package com.example.searchlistcomposeexample.repository

import com.example.searchlistcomposeexample.network.ApiService


class MainRepository(private val service: ApiService) {

    suspend fun getData() = service.getData()

}