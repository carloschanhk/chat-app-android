package com.carloscoding.chatapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import com.carloscoding.chatapp.data.model.ChatRoom
import com.carloscoding.chatapp.databinding.HomeFragmentBinding
import com.carloscoding.chatapp.ui.chat.ChatActivity
import com.carloscoding.chatapp.ui.chat.ChatActivity.Companion.CHATROOM_DATA
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HomeFragment.viewModel
            rvChatroomList.apply {
                adapter = ChatRoomAdapter().apply {
                    onChatRoomClickListener = ::onChatRoomClicked
                }
                addItemDecoration(DividerItemDecoration(this.context, LinearLayout.VERTICAL))
            }
        }
        return binding.root
    }

     private fun onChatRoomClicked(chatroom: ChatRoom) {
        val intent = Intent(activity, ChatActivity::class.java).apply {
            putExtra(CHATROOM_DATA, chatroom)
        }
        startActivity(intent)
    }
}
