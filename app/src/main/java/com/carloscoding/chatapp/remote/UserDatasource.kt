package com.carloscoding.chatapp.remote

import com.carloscoding.chatapp.data.model.User
import com.carloscoding.chatapp.remote.dto.UserDto
import com.carloscoding.chatapp.remote.request.SignupRequest
import com.carloscoding.chatapp.util.Response
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatasource @Inject constructor(private val httpClient: HttpClient) {
    suspend fun login(name: String, password: String): Response<User> {
        return try {
            val result = httpClient.get<UserDto>(path = "/user/login") {
                parameter("name", name)
                parameter("password", password)
            }.toUser()
            Response.Success(result)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    suspend fun signup(name: String, password: String): Response<User> {
        return try {
            val result: User = httpClient.post<UserDto>(path = "/user/signup") {
                contentType(ContentType.Application.Json)
                body = SignupRequest(name, password)
            }.toUser()
            Response.Success(result)
        } catch (e: Exception) {
            Response.Error(e)

        }
    }
}