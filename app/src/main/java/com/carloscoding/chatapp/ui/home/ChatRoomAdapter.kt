package com.carloscoding.chatapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carloscoding.chatapp.data.model.ChatRoom
import com.carloscoding.chatapp.databinding.ChatroomLayoutBinding

class ChatRoomAdapter : ListAdapter<ChatRoom, ChatRoomAdapter.ChatRoomViewHolder>(Companion) {
    class ChatRoomViewHolder(val binding: ChatroomLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onChatRoomClickListener: ((ChatRoom)-> Unit)? = null

    companion object : DiffUtil.ItemCallback<ChatRoom>() {
        override fun areItemsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatRoom, newItem: ChatRoom): Boolean {
            return oldItem.lastMessage == newItem.lastMessage
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        return ChatRoomViewHolder(
            ChatroomLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatroom = getItem(position)
        holder.apply {
            binding.chatroom = chatroom
            itemView.setOnClickListener { onChatRoomClickListener?.invoke(chatroom) }
        }

    }
}