package com.carloscoding.chatapp.remote.dto

import com.carloscoding.chatapp.data.model.Message
import com.carloscoding.chatapp.util.convertToDateString
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    val id: Int,
    val userId: Int,
    val username: String,
    val roomId: Int,
    val createdAt: Long,
    val text: String,
    val isRead: Boolean,
) {
    fun toMessage(userId: Int): Message {
        return Message(
            id,
            username,
            roomId,
            createdAt,
            createdAt.convertToDateString(),
            text,
            isRead,
            this.userId == userId,
        )
    }
}
