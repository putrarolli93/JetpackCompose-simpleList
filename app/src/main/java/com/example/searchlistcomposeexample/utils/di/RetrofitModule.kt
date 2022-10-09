package com.example.testapp.utils.di

import com.example.searchlistcomposeexample.BuildConfig
import com.example.searchlistcomposeexample.network.ApiService
import com.example.searchlistcomposeexample.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val retrofitModule = module {    // 1

    single {   // 2
        OkHttpClient.Builder().build()  // 3
    }
    single {
        retrofit(Constants.BASE_URL)  // 4
    }
    single {
        get<Retrofit>().create(ApiService::class.java)   // 5
    }
}

const val connectTimeout: Long = 40
const val readTimeout: Long = 40

fun provideHttpClient(): OkHttpClient {
    val okHttpClientBuilder = OkHttpClient.Builder()
        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
    var httpLoggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    } else {
        httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }
    okHttpClientBuilder.build()
    return okHttpClientBuilder.addInterceptor(httpLoggingInterceptor).build()
}

private fun retrofit(baseUrl: String) = Retrofit.Builder()
    .callFactory(OkHttpClient.Builder().build())
    .baseUrl(baseUrl)
//    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())  // 6
    .client(provideHttpClient())
    .build()