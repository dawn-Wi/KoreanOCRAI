package com.gausslab.koreanocrai.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FastApi {
    @POST("/image")
    suspend fun sendImage(@Body request: ImageRequest): Response<String>
}