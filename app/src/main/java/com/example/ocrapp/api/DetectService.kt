package com.example.ocrapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object DetectService {

    private val logger = HttpLoggingInterceptor()
    private val retrofit: Retrofit
    private val api: DetectAPI

    init {

        logger.level = HttpLoggingInterceptor.Level.BODY

        retrofit = Retrofit.Builder()
            .baseUrl("35.224.180.242:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(OkHttpClient().newBuilder().addInterceptor(logger).build())
            .build()

        api = retrofit.create(DetectAPI::class.java)

    }

}