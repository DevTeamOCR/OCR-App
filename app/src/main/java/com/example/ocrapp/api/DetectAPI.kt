package com.example.ocrapp.api

import com.squareup.okhttp.RequestBody
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part

interface DetectAPI {

    @POST("detect")
    suspend fun detect(@Part image: MultipartBody.Part): Response<Detection>

}