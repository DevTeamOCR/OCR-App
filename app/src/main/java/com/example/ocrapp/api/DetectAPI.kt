package com.example.ocrapp.api

import retrofit2.Response
import retrofit2.http.POST

interface DetectAPI {

    @POST("detect")
    suspend fun detect(): Response<Detection>

}