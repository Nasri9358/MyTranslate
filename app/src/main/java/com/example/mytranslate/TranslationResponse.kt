package com.example.mytranslate


data class TranslationResponse(val translations: List<Translation>) {
    data class Translation(val text: String)
}


