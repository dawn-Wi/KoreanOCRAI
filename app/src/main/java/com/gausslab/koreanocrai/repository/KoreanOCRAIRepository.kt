package com.gausslab.koreanocrai.repository

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.gausslab.koreanocrai.model.Constants.BASE_URL
import com.gausslab.koreanocrai.remote.FastApi
import com.gausslab.koreanocrai.remote.ImageRequest
import dagger.hilt.android.scopes.ActivityScoped
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun sendImage(userImage: ImageBitmap): String{
        val result = fastApi.sendImage(changeBitmapToRequestBody(userImage.asAndroidBitmap()))
        return if (result.isSuccessful){
            val koreanString = result.body() ?: throw IllegalStateException("Response body is null")
            koreanString
        }else{
            throw IOException("Failed to sendImage: ${result.code()}")
        }
    }

    private fun changeBitmapToRequestBody(bitmap: Bitmap): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        val requestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), byteArray)

        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

}