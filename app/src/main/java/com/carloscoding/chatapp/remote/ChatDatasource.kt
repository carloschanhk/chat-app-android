package com.carloscoding.chatapp.remote

import com.carloscoding.chatapp.data.model.ChatRoom
import com.carloscoding.chatapp.remote.dto.ChatRoomDto
import com.carloscoding.chatapp.remote.request.CreateRoomRequest
import com.carloscoding.chatapp.util.Response
import io.ktor.client.*
import io.ktor.client.request.*

import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatDatasource @Inject constructor(private val httpClient: HttpClient) {

    suspend fun getAllChatRooms(userId: Int): Response<List<ChatRoom>> {
        return try {
            val chatRooms = httpClient.get<List<ChatRoomDto>>(path = "/chatroom") {
                parameter("userId", userId)
            }.map { it.toChatRoom(userId) }.sortedBy { it.createdAt }
            Response.Success(chatRooms)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    suspend fun createRoom(userId: Int, partnerId: Int): Response<ChatRoom> {
        return try {
            val chatroom: ChatRoom = httpClient.post<ChatRoomDto>(path = "/chatroom/create") {
                body = CreateRoomRequest(userId, partnerId)
            }.toChatRoom(userId)
            Response.Success(chatroom)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }
}