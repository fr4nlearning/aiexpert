package com.example.cinescan.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIRequestDto(
    val model: String,
    val messages: List<MessageDto>,
    val max_tokens: Int = 500
)

@Serializable
data class MessageDto(
    val role: String,
    val content: List<ContentDto>
)

@Serializable
data class ContentDto(
    val type: String,
    val text: String? = null,
    val image_url: ImageUrlDto? = null
)

@Serializable
data class ImageUrlDto(
    val url: String
)

