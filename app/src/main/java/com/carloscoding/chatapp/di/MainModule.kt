package com.carloscoding.chatapp.di

import com.carloscoding.chatapp.remote.exception.BadRequestException
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(Logging)
            install(WebSockets)
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            defaultRequest {
                host = "192.168.50.99"
                port = 8080
            }
            HttpResponseValidator {
                handleResponseException { exception ->
                    val clientException =
                        exception as? ClientRequestException ?: return@handleResponseException
                    val exceptionResponse = clientException.response
                    if (exceptionResponse.status == HttpStatusCode.BadRequest) {
                        throw BadRequestException(exceptionResponse.readText())
                    }
                }
            }
        }
    }
}