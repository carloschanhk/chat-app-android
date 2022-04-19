package com.carloscoding.chatapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carloscoding.chatapp.data.UserRepository
import com.carloscoding.chatapp.remote.exception.BadRequestException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _onLoginResponse = MutableSharedFlow<LoginResponse>()
    val onLoginResponse = _onLoginResponse.asSharedFlow()

    fun onLogin(username: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(username, password)
            result.takeIfSuccess()?.let {
                _onLoginResponse.emit(LoginResponse.Success)
            } ?: run {
                _onLoginResponse.emit(LoginResponse.Error(result.takeError()))
            }
        }
    }

    sealed class LoginResponse {
        object Success : LoginResponse()
        data class Error(
            val exception: Exception,
        ) : LoginResponse()
    }
}