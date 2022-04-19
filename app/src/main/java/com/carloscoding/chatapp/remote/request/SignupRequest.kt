package com.carloscoding.chatapp.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SignupRequest(
    val name: String,
    val password: String,
)