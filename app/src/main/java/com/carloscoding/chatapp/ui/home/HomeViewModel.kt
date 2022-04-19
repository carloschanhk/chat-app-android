package com.carloscoding.chatapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloscoding.chatapp.data.ChatRepository
import com.carloscoding.chatapp.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ViewModel() {
    val chatRooms = chatRepository.getChatRoomFlow().asStateFlow()

    init {
        try {
            viewModelScope.launch {
                chatRepository.observeMessage()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}