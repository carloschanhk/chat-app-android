package com.carloscoding.chatapp.remote.dto

import com.carloscoding.chatapp.data.model.ChatRoom
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomDto(
    val id: Int,
    val title: String?,
    val createdAt: Long,
    val participants: List<UserDto>,
    val lastMessage: MessageDto? = null
) {
    fun toChatRoom(userId: Int): ChatRoom {
        return ChatRoom(
            id,
            title ?: participants.first { it.id != userId }.name,
            createdAt,
            lastMessage?.toMessage(userId),
            participants.map { it.toUser() },
        )
    }
}
