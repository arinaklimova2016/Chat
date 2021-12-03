package com.example.chat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.constants.Constants.VIEWTYPE1
import com.example.chat.constants.Constants.VIEWTYPE2
import com.example.chat.databinding.ReceiveMessageBinding
import com.example.chat.databinding.SendMessageBinding
import com.example.chat.model.MessageDto
import com.example.chat.model.User

class ChatAdapter(
    private val message: LiveData<List<MessageDto>>,
    private val receiver: User,
    private val you: User
) : ListAdapter<MessageDto, ChatAdapter.MyViewHolder>(DiffCallback()) {

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
        return if (currentList[position].from == you) VIEWTYPE1 else VIEWTYPE2
    }

    abstract class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(messageDto: MessageDto, user: User)
    }

    class SendViewHolder(binding: SendMessageBinding) : MyViewHolder(binding.root) {

        private val bindingSend: SendMessageBinding = binding

        override fun bind(messageDto: MessageDto, user: User) {
            bindingSend.txtSendMessage.text = messageDto.message
            bindingSend.txtUsername.text = user.name
        }
    }

    class ReceiveViewHolder(binding: ReceiveMessageBinding) : MyViewHolder(binding.root) {

        private val bindingReceive: ReceiveMessageBinding = binding

        override fun bind(messageDto: MessageDto, user: User) {
            bindingReceive.txtReceiveMessage.text = messageDto.message
            bindingReceive.txtUsername.text = user.name
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