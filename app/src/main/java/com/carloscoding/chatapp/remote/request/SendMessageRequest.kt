package com.carloscoding.chatapp.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val roomId: Int,
    val userId: Int,
    val content: String,
)