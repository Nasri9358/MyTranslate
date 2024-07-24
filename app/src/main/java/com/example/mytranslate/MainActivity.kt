package com.example.mytranslate

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mytranslate.ui.theme.MyTranslateTheme
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var translateApi: TranslateApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputText: EditText = findViewById(R.id.inputText)
        val translateButton: Button = findViewById(R.id.translateButton)
        val translatedText: TextView = findViewById(R.id.translatedText)

        // Инициализация Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://translation.googleapis.com/language/translate/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        translateApi = retrofit.create(TranslateApi::class.java)

        translateButton.setOnClickListener {
            val textToTranslate = inputText.text.toString()
            translate(textToTranslate, translatedText)
        }
    }

    private fun translate(text: String, translatedTextView: TextView) {
        val call = translateApi.translate(text, "es", "YOUR_API")
        call.enqueue(object : Callback<TranslationResponse> {
            override fun onResponse(call: Call<TranslationResponse>, response: Response<TranslationResponse>) {
                if (response.isSuccessful) {
                    val translatedText = response.body()?.data?.translations?.firstOrNull()?.translatedText
                    translatedTextView.text = translatedText
                } else {
                    translatedTextView.text = "Translation failed"
                }
            }

            override fun onFailure(call: Call<TranslationResponse>, t: Throwable) {
                translatedTextView.text = "Error: ${t.message}"
            }
        })
    }
}
