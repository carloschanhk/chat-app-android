package com.carloscoding.chatapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    val id: Int,
    val username: String,
    val roomId: Int,
    val createdAt: Long,
    val formattedDate: String,
    val text: String,
    val isRead: Boolean,
    val isSelf: Boolean,
) : Parcelable