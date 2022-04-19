package com.carloscoding.chatapp.remote.dto

import com.carloscoding.chatapp.data.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val name: String,
    val createdAt: Long,
) {
    fun toUser(): User {
        return User(id, name)
    }
}
