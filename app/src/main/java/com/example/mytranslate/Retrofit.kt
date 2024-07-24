package com.example.mytranslate

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    @GET("translate")
    fun translate(
        @Query("q") query: String,
        @Query("target") targetLanguage: String,
        @Query("key") apiKey: String
    ): Call<TranslationResponse>
}
