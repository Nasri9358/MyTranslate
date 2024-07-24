package com.example.mytranslate

data class TranslationResponse(val data: Data) {
    data class Data(val translations: List<Translation>) {
        data class Translation(val translatedText: String)
    }
}

