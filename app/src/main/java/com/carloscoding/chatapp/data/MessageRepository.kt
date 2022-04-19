package com.carloscoding.chatapp.data

import android.util.Log
import com.carloscoding.chatapp.data.model.Message
import com.carloscoding.chatapp.remote.MessageDatasource
import com.carloscoding.chatapp.remote.request.SendMessageRequest
import com.carloscoding.chatapp.util.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

class MessageRepository @Inject constructor(
    private val messageDatasource: MessageDatasource,
    private val userRepository: UserRepository,
) {
    private var currentRoomId: Int? = null
    private var roomMessages: MutableList<Message> = mutableListOf()
    private val roomMessagesFlow: MutableStateFlow<List<Message>> = MutableStateFlow(listOf())
    private var observeMessagesScope: CoroutineScope? = null

    private suspend fun getMessagesByRoom() {
        try {
            val result = currentRoomId?.let {
                messageDatasource.getMessagesByRoom(
                    it,
                    userRepository.getUser().id
                )
            }
                ?: throw Exception("room id is not initialized")
            result.takeIfSuccess()?.let {
                roomMessages = it.toMutableList()
            } ?: throw result.takeError()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun sendMessage(message: String): Response<Unit> {
        return messageDatasource.sendMessage(
            SendMessageRequest(
                roomId = currentRoomId
                    ?: return Response.Error(Exception("room id is not initialized")),
                userId = userRepository.getUser().id,
                content = message,
            )
        )
    }

    suspend fun observeMessage() {
            messageDatasource.getMessageFlow().onStart {
                getMessagesByRoom()
                roomMessagesFlow.emit(roomMessages.toList())
            }.filter {
                it.roomId == currentRoomId
            }.catch {
                // TODO: ReFetch
                roomMessagesFlow.emit(roomMessages.toList())
            }.collect { message ->
                roomMessages.add(message)
                roomMessagesFlow.emit(roomMessages.toList())
            }
    }

    private fun cancelObserveMessage() {
        observeMessagesScope?.cancel()
        observeMessagesScope = null
    }

    fun getMessagesFlow(roomId: Int): MutableStateFlow<List<Message>> {
        currentRoomId = roomId
        return roomMessagesFlow
    }

    suspend fun leaveRoom() {
        cancelObserveMessage()
        currentRoomId = null
        roomMessages.clear()
        roomMessagesFlow.emit(emptyList())
    }

}