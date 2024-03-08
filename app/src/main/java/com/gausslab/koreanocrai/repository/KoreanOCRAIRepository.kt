package com.gausslab.koreanocrai.repository

import androidx.compose.ui.graphics.ImageBitmap
import com.gausslab.koreanocrai.model.Constants.BASE_URL
import com.gausslab.koreanocrai.remote.FastApi
import com.gausslab.koreanocrai.remote.ImageRequest
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

@ActivityScoped
class KoreanOCRAIRepository {
    private var fastApi: FastApi

    init {
        val thisRetro =
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
        fastApi = thisRetro.create(FastApi::class.java)
    }

    suspend fun sendImage(userImage: ImageBitmap): String{
        val result = fastApi.sendImage(ImageRequest(userImage))
        return if (result.isSuccessful){
            val koreanString = result.body() ?: throw IllegalStateException("Response body is null")
            koreanString
        }else{
            throw IOException("Failed to sendImage: ${result.code()}")
        }
    }

}