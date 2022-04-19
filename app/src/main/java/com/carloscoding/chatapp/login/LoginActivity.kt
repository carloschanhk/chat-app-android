package com.carloscoding.chatapp.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.carloscoding.chatapp.R
import com.carloscoding.chatapp.databinding.ActivityLoginBinding
import com.carloscoding.chatapp.remote.exception.BadRequestException
import com.carloscoding.chatapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@LoginActivity
            activity = this@LoginActivity
        }
        setContentView(binding.root)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onLoginResponse.collect {
                    when (it) {
                        is LoginViewModel.LoginResponse.Success -> navigateToMainActivity()
                        is LoginViewModel.LoginResponse.Error -> {
                            var message: String? = null
                            when (it.exception) {
                                is BadRequestException -> message = it.exception.message
                                is IOException -> message = resources.getString(R.string.generic_connection_error)
                            }
                            showErrorMessage(message)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun showErrorMessage(message: String?) {
        binding.tvErrorMessage.apply {
            visibility = View.VISIBLE
            text = message ?: resources.getString(R.string.generic_error_message)
        }

    }

    fun onLoginButtonClicked() {
        with(binding) {
            if (etLoginUsername.text.isNotBlank() && etLoginPassword.text.isNotBlank()) {
                viewModel.onLogin(etLoginUsername.text.toString(), etLoginPassword.text.toString())
            } else {
                showErrorMessage(resources.getString(R.string.login_empty_input_error))
            }
            etLoginUsername.text.clear()
            etLoginPassword.text.clear()
        }
    }
}