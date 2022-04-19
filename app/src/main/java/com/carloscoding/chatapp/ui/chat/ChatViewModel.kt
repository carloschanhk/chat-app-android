package com.carloscoding.chatapp.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloscoding.chatapp.data.MessageRepository
import com.carloscoding.chatapp.data.model.ChatRoom
import com.carloscoding.chatapp.remote.request.SendMessageRequest
import com.carloscoding.chatapp.ui.chat.ChatActivity.Companion.CHATROOM_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val chatroom: ChatRoom = savedStateHandle[CHATROOM_DATA]!!
    val messages = messageRepository.getMessagesFlow(chatroom.id).asStateFlow()

    val messageText = MutableStateFlow("")

    init {
        viewModelScope.launch {
            messageRepository.observeMessage()
        }
    }

    fun onSentButtonClicked(){
        viewModelScope.launch {
            messageRepository.sendMessage(messageText.value)
            messageText.value = ""
        }
    }
}