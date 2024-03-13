package com.gausslab.koreanocrai.repository

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.gausslab.koreanocrai.model.Constants.BASE_URL
import com.gausslab.koreanocrai.remote.PredictionResponse
import com.gausslab.koreanocrai.remote.FastApi
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
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

    suspend fun sendImage(userImage: ImageBitmap): PredictionResponse {
        val result = fastApi.sendImage(changeBitmapToRequestBody(userImage.asAndroidBitmap()))

        return if (result.isSuccessful) {
            val responseBody = result.body()
            responseBody ?: throw IllegalStateException("Response body is null")
        } else {
            throw IOException("Failed to sendImage: ${result.code()}")
        }
    }


    suspend fun saveLabellingImage(userLabellingImage: ImageBitmap, label: String){
        val imageRequestBody = changeBitmapToRequestBody(userLabellingImage.asAndroidBitmap())
        fastApi.saveLabellingImage(label,imageRequestBody)
    }

    private fun changeBitmapToRequestBody(bitmap: Bitmap): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)

        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

}