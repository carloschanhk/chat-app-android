package com.carloscoding.chatapp.data

import android.util.Log
import com.carloscoding.chatapp.data.model.ChatRoom
import com.carloscoding.chatapp.remote.ChatDatasource
import com.carloscoding.chatapp.remote.MessageDatasource
import com.carloscoding.chatapp.util.Response
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

class ChatRepository @Inject constructor(
    private val chatDatasource: ChatDatasource,
    private val messageDatasource: MessageDatasource,
    private val userRepository: UserRepository,
) {
    private var chatRooms: MutableList<ChatRoom> = mutableListOf()
    private val chatRoomsFlow: MutableStateFlow<List<ChatRoom>> = MutableStateFlow(chatRooms)
    private var observeMessageScope: CoroutineScope? = null

    suspend fun createRoom(userId: Int, partnerId: Int): Response<ChatRoom> {
        return try {
            val user = userRepository.getUser()
            val result = chatDatasource.createRoom(user.id, partnerId)
            val newChatRoom = result.takeIfSuccess()?.apply {
                chatRooms.add(this)
                chatRoomsFlow.emit(chatRooms)
            } ?: throw result.takeError()
            Response.Success(newChatRoom)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    fun getChatRoomFlow(): MutableStateFlow<List<ChatRoom>> = chatRoomsFlow

    private suspend fun getChatRoomsByUser() {
        try {
            val user = userRepository.getUser()
            val result = chatDatasource.getAllChatRooms(user.id)
            result.takeIfSuccess()?.let {
                chatRooms = it.toMutableList()
            } ?: throw result.takeError()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun initConnection() {
        messageDatasource.observeNewMessages(userRepository.getUser().id)
    }

    suspend fun observeMessage() {
        try{
            messageDatasource.getMessageFlow().onStart {
                getChatRoomsByUser()
                chatRoomsFlow.emit(chatRooms.toList())
            }.catch {
                chatRoomsFlow.emit(chatRooms.toList())
            }.collect { message ->
                // Change the latest message of the chatroom
                chatRooms.find { it.id == message.roomId }?.let {
                    chatRooms.remove(it)
                    chatRooms.add(it.copy(lastMessage = message))
                } ?: run {
                    // Fetch entire chatroom list if it is a new chat
                    getChatRoomsByUser()
                }
                chatRoomsFlow.emit(chatRooms.toList())
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun cancelObserveMessage() {
        observeMessageScope?.cancel()
        observeMessageScope = null
    }

    suspend fun onLogout() {
        cancelObserveMessage()
        messageDatasource.closeSession()
        chatRooms.clear()
        chatRoomsFlow.emit(emptyList())
    }
}