package com.example.chat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.constants.Constants.VIEWTYPE1
import com.example.chat.constants.Constants.VIEWTYPE2
import com.example.chat.databinding.ReceiveMessageBinding
import com.example.chat.databinding.SendMessageBinding
import com.example.chat.model.User
import com.example.chat.room.Message

class ChatAdapter(
    private val receiver: User,
    private val you: User
) : ListAdapter<Message, ChatAdapter.MyViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return if (viewType == VIEWTYPE1) {
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val whatType = if (getItemViewType(position) == VIEWTYPE1) {
            you
        } else {
            receiver
        }
        holder.bind(currentList[position], whatType)
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position].from == you.id) VIEWTYPE1 else VIEWTYPE2
    }

    abstract class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(message: Message, user: User)
    }

    class SendViewHolder(binding: SendMessageBinding) : MyViewHolder(binding.root) {
        private val bindingSend: SendMessageBinding = binding

        override fun bind(message: Message, user: User) {
            bindingSend.txtSendMessage.text = message.message
            bindingSend.txtUsername.text = user.name
        }
    }

    class ReceiveViewHolder(binding: ReceiveMessageBinding) : MyViewHolder(binding.root) {
        private val bindingReceive: ReceiveMessageBinding = binding

        override fun bind(message: Message, user: User) {
            bindingReceive.txtReceiveMessage.text = message.message
            bindingReceive.txtUsername.text = user.name
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldMessge: Message, newMessge: Message): Boolean {
            return oldMessge == newMessge
        }

        override fun areContentsTheSame(oldMessge: Message, newMessge: Message): Boolean {
            return oldMessge == newMessge
        }
    }

}