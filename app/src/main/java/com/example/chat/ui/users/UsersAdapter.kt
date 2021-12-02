package com.example.chat.ui.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.databinding.UserLayoutBinding
import com.example.chat.model.User
import com.example.chat.model.UsersReceivedDto

class UsersAdapter(
    private val onUsersClicked: (User) -> Unit
) : ListAdapter<User, UsersAdapter.MyViewHolder>(DiffCallback()) {

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

        fun bind(user: User, onClick: (User) -> Unit) {
            binding.user.text = user.name
            binding.root.setOnClickListener {
                onClick(user)
            }
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
            return oldUser == newUser
        }

        override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
            return oldUser == newUser
        }

    }

}