package com.demo.assignmentdemo.NetworkConnection

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBase {
    private const val URL = "https://jsonplaceholder.typicode.com/"

    private val retrofitClient: Retrofit.Builder by lazy {

        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
            .addInterceptor(logger)

        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttp.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ServiceInterface by lazy {
        retrofitClient
            .build()
            .create(ServiceInterface::class.java)
    }
}