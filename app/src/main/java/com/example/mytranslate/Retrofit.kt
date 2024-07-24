package com.example.mytranslate

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface TranslateApi {

    @Headers("Content-Type: application/json")
    @POST("translate/v2/translate")
    fun translate(
        @Body request: TranslateRequest
    ): Call<TranslationResponse>
}

data class TranslateRequest(
    val texts: List<String>,
    val targetLanguageCode: String,
    val folderId: String
)
