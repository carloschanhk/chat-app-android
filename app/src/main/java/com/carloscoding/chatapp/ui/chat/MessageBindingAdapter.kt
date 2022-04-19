package com.carloscoding.chatapp.ui.chat

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carloscoding.chatapp.data.model.Message

@BindingAdapter(value = ["data"], requireAll = true)
fun bindDataToMessagesRV(recyclerView: RecyclerView, data: List<Message>) {
    val adapter = recyclerView.adapter as MessageAdapter
    adapter.submitList(data)
    if (data.lastOrNull()?.isSelf == true){
        recyclerView.layoutManager?.smoothScrollToPosition(recyclerView, null, data.size)
    }
}