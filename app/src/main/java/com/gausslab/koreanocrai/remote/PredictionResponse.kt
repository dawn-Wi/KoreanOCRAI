package com.gausslab.koreanocrai.remote

import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @SerializedName("prediction")
    var answer: String = "",

    @SerializedName("probability")
    var probability: Float = 0f
)
