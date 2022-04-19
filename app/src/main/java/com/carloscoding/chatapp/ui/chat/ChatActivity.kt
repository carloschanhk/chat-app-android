package com.carloscoding.chatapp.ui.chat

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carloscoding.chatapp.R
import com.carloscoding.chatapp.data.model.ChatRoom
import com.carloscoding.chatapp.databinding.ActivityChatBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    companion object {
        const val CHATROOM_DATA = "chatroom_data"
    }

    private val viewModel: ChatViewModel by viewModels()

    lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@ChatActivity
            viewModel = this@ChatActivity.viewModel
            rvMessages.apply {
                adapter = MessageAdapter()
//                layoutManager = LinearLayoutManager(this@ChatActivity).apply {
//                    smoothScrollToPosition(this@apply, null )
//                }
            }
        }
        setContentView(binding.root)
        setSupportActionBar(binding.chatToolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}