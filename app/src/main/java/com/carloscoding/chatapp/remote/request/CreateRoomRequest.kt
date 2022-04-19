package com.carloscoding.chatapp.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    val userId: Int,
    val partnerId: Int,
)
