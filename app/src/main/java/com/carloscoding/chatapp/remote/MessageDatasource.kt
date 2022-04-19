package com.carloscoding.chatapp.remote

import android.util.Log
import com.carloscoding.chatapp.data.model.Message
import com.carloscoding.chatapp.remote.dto.MessageDto
import com.carloscoding.chatapp.remote.request.SendMessageRequest
import com.carloscoding.chatapp.util.Response
import dagger.Component
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageDatasource @Inject constructor(private val httpClient: HttpClient) {
    private var socket: WebSocketSession? = null
    private val messageFlow = MutableSharedFlow<Message>()
    private var isObservingMessage: Boolean = false

    private suspend fun initSession(userId: Int): Response<Unit> {
        return try {
            socket = httpClient.webSocketSession(path = "/messages?userId=$userId")
            if (socket?.isActive == true) {
                Response.Success(Unit)
            } else {
                throw Exception("Unable to establish a connection")
            }
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest): Response<Unit> {
        return try {
            socket?.send(Frame.Text(Json.encodeToString(sendMessageRequest)))
            Response.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Response.Error(e)
        }
    }

    suspend fun observeNewMessages(userId: Int) {
        try {
            if (socket == null) {
                initSession(userId).takeIfError()?.let {
                    throw it
                }
            }
            if (!isObservingMessage) {
                isObservingMessage = true
                socket?.incoming
                    ?.receiveAsFlow()
                    ?.filter { it is Frame.Text }
                    ?.map {
                        val json = (it as? Frame.Text)?.readText() ?: ""
                        Json.decodeFromString<MessageDto>(json).toMessage(userId)
                    }?.collect {
                        messageFlow.emit(it)
                    }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMessageFlow(): MutableSharedFlow<Message> = messageFlow

    suspend fun closeSession() {
        socket?.close()
        isObservingMessage = false
    }

    suspend fun getMessagesByRoom(roomId: Int, userId: Int): Response<List<Message>> {
        return try {
            val messages: List<Message> =
                httpClient.get<List<MessageDto>>(path = "/messages") {
                    parameter("roomId", roomId)
                }.map { it.toMessage(userId) }.sortedBy { it.createdAt }
            Response.Success(messages)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }
}