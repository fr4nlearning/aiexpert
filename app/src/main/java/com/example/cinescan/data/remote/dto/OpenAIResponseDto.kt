package com.example.cinescan.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OpenAIResponseDto(
    val id: String? = null,
    val model: String? = null,
    val choices: List<ChoiceDto>
)

@Serializable
data class ChoiceDto(
    val message: ResponseMessageDto,
    val finish_reason: String? = null
)

@Serializable
data class ResponseMessageDto(
    val role: String,
    val content: String
)

