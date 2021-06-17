package com.example.ocrapp.api

import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface DetectAPI {

    @Multipart
    @POST("detect")
    fun detect(@Part image: MultipartBody.Part): Deferred<DetectionResponse>

}