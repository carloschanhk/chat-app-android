package com.carloscoding.chatapp.ui.home

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carloscoding.chatapp.data.model.ChatRoom

@BindingAdapter(value = ["data"], requireAll = true)
fun bindDataToChatroomRV(recyclerView: RecyclerView, data: List<ChatRoom>) {
    val adapter = recyclerView.adapter as ChatRoomAdapter
    adapter.submitList(data.sortedByDescending { it.lastMessage?.createdAt })
}