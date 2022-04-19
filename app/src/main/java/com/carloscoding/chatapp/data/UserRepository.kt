package com.carloscoding.chatapp.data

import com.carloscoding.chatapp.data.model.User
import com.carloscoding.chatapp.remote.UserDatasource
import com.carloscoding.chatapp.util.Response
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDatasource: UserDatasource) {
    private var user: User? = null

    fun getUser(): User {
        return user ?: throw Exception("Something went wrong, please login again")
    }

    suspend fun login(name: String, password: String): Response<User> {
        return try {
            val result = userDatasource.login(name, password)
            val user = result.takeIfSuccess()?.apply {
                user = this
            } ?: throw result.takeError()
            Response.Success(user)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

    suspend fun signup(name: String, password: String): Response<User> {
        return try {
            val result = userDatasource.signup(name, password)
            val user = result.takeIfSuccess()?.apply {
                user = this
            } ?: throw result.takeError()
            Response.Success(user)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }
}