package com.example.chat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.ReceiveMessageBinding
import com.example.chat.databinding.SendMessageBinding
import com.example.chat.model.UiUser
import com.example.data.data.room.Message

class ChatAdapter(
    private val receiver: UiUser,
    private val you: UiUser
) : ListAdapter<Message, ChatAdapter.BaseViewHolder>(DiffCallback()) {

    companion object {
        private const val SENDMESSAGE = 0
        private const val RECEIVEMESSAGE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == SENDMESSAGE) {
            SendViewHolder(
                SendMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            ReceiveViewHolder(
                ReceiveMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val whoWroteTheMessage = if (getItemViewType(position) == SENDMESSAGE) {
            you
        } else {
            receiver
        }
        holder.bind(currentList[position], whoWroteTheMessage)
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].from == you.id) SENDMESSAGE else RECEIVEMESSAGE
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(message: Message, user: UiUser)
    }

    class SendViewHolder(
        private val binding: SendMessageBinding
    ) : BaseViewHolder(binding.root) {

        override fun bind(message: Message, user: UiUser) {
            with(binding) {
                txtSendMessage.text = message.message
                txtUsername.text = user.name
            }
        }
    }

    class ReceiveViewHolder(
        private val binding: ReceiveMessageBinding
    ) : BaseViewHolder(binding.root) {

        override fun bind(message: Message, user: UiUser) {
            with(binding) {
                txtReceiveMessage.text = message.message
                txtUsername.text = user.name
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldMessge: Message, newMessge: Message): Boolean {
            return oldMessge.id == newMessge.id
        }

        override fun areContentsTheSame(oldMessge: Message, newMessge: Message): Boolean {
            return oldMessge == newMessge
        }
    }

}