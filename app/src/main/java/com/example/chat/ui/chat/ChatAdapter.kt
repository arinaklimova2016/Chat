package com.example.chat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.SendMessageBinding
import com.example.chat.model.MessageDto

class ChatAdapter : ListAdapter<MessageDto, ChatAdapter.MyViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SendMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder()
    }

    override fun onBindViewHolder(holder: ChatAdapter.MyViewHolder, position: Int) {
        holder.bind()
    }

    abstract class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {

        }

    }

    class DiffCallback : DiffUtil.ItemCallback<MessageDto>() {

        override fun areItemsTheSame(oldMessge: MessageDto, newMessge: MessageDto): Boolean {
            return oldMessge == newMessge
        }

        override fun areContentsTheSame(oldMessge: MessageDto, newMessge: MessageDto): Boolean {
            return oldMessge == newMessge
        }

    }

}