package com.carloscoding.chatapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatRoom(
    val id: Int,
    val title: String,
    val createdAt: Long,
    var lastMessage: Message?,
    val participants: List<User>,
) : Parcelable
