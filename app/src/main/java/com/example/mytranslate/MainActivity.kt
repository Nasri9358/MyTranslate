package com.example.mytranslate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var translateApi: TranslateApi
    private val iamToken = "t1.9euelZrGjYmVmZiTns-Slsmbl8fHjO3rnpWai5qKksuZzc-amZXGx5eTzZnl8_dBUXpK-e8sABxW_t3z9wEAeEr57ywAHFb-zef1656VmseQjsyZnYqajoyejpaWic7P7_zF656VmseQjsyZnYqajoyejpaWic7P.fCEt7vyaB6NR8H2G4fYUp6E-jopkcIo5yH9wDoPu0K8k5X1Sw31cywifTikp2XUY_oxtlTandK-g0-6No_gmAA" // Замените на ваш действующий IAM токен
    private val folderId = "b1gih97qg7s9hb5v0ui5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText: EditText = findViewById(R.id.inputText)
        val translateButton: Button = findViewById(R.id.translateButton)
        val translatedText: TextView = findViewById(R.id.translatedText)

        // Инициализация Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://translate.api.cloud.yandex.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $iamToken")
                    .build()
                chain.proceed(request)
            }.build())
            .build()

        translateApi = retrofit.create(TranslateApi::class.java)

        translateButton.setOnClickListener {
            val textToTranslate = inputText.text.toString()
            translate(textToTranslate, translatedText)
        }
    }

    private fun translate(text: String, translatedTextView: TextView) {
        val request = TranslateRequest(texts = listOf(text), targetLanguageCode = "en", folderId = folderId)
        val call = translateApi.translate(request)
        call.enqueue(object : Callback<TranslationResponse> {
            override fun onResponse(call: Call<TranslationResponse>, response: Response<TranslationResponse>) {
                if (response.isSuccessful) {
                    val translatedText = response.body()?.translations?.firstOrNull()?.text
                    Log.d("TRANSLATE_SUCCESS", "Translated Text: $translatedText")
                    translatedTextView.text = translatedText ?: "Translation failed"
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API_ERROR", errorBody)
                    translatedTextView.text = "Translation failed: $errorBody"
                }
            }

            override fun onFailure(call: Call<TranslationResponse>, t: Throwable) {
                Log.e("API_CALL_FAILURE", t.message ?: "Unknown error")
                translatedTextView.text = "Error: ${t.message}"
            }
        })
    }
}