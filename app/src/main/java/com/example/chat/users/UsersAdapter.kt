package com.example.chat.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.databinding.UserLayoutBinding
import com.example.chat.model.UsersReceivedDto

class UsersAdapter(
    private val onUsersClicked: (UsersReceivedDto) -> Unit
) : ListAdapter<UsersReceivedDto, UsersAdapter.MyViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.MyViewHolder {
        val binding = UserLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersAdapter.MyViewHolder, position: Int) {
        holder.bind(currentList[position], onUsersClicked)
    }

    class MyViewHolder(private val binding: UserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UsersReceivedDto, onClick: (UsersReceivedDto) -> Unit) {
            binding.user.text = binding.root.context.getString(
                R.string.email
            )
            binding.root.setOnClickListener {
                onClick(user)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<UsersReceivedDto>() {

        override fun areItemsTheSame(oldUser: UsersReceivedDto, newUser: UsersReceivedDto): Boolean {
            return oldUser.users == newUser.users
        }

        override fun areContentsTheSame(oldUser: UsersReceivedDto, newUser: UsersReceivedDto): Boolean {
            return oldUser == newUser
        }

    }

}