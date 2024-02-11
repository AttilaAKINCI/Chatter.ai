package com.akinci.chatter.core.ai

import com.akinci.chatter.core.application.AppConfig
import com.google.ai.client.generativeai.GenerativeModel
import timber.log.Timber
import javax.inject.Inject

class GeminiAI @Inject constructor(
    private val appConfig: AppConfig,
) {
    private val generativeModel by lazy {
        GenerativeModel(modelName = "gemini-pro", apiKey = appConfig.getGeminiAPIKey())
    }

    suspend fun ask(question: String) = runCatching {
        generativeModel.generateContent(prompt = question).text
    }.onFailure {
        Timber.e(it, "Exception is occurred on ai response")
    }
}
