package com.example.chat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.constants.Constants.USERID
import com.example.chat.constants.Constants.USERNAME
import com.example.chat.databinding.FragmentChatBinding
import com.example.chat.model.User
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val model by viewModel<ChatViewModel>()

    private val itemUser: User by lazy {
        User(
            requireArguments().getString(USERID)!!,
            requireArguments().getString(USERNAME)!!
        )
    }

    private val adapter by lazy {
        ChatAdapter(
            message = model.message,
            receiver = itemUser,
            you = model.getYou()
        )
    }

    private lateinit var txtView: TextView
    private lateinit var editText: EditText
    private lateinit var btnSend: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingRecycler()

        txtView = binding.username
        editText = binding.editMessage
        btnSend = binding.btnSendMessage

        txtView.text = itemUser.name
        btnSend.setOnClickListener{
            val message: String = editText.text.toString()
            model.sendMessage(itemUser, message)
            editText.text.clear()
        }

        observeToViewModel()

    }

    private fun observeToViewModel() {
        model.message.observe(viewLifecycleOwner, {
            adapter.submitList(it ?: listOf())
        })
    }

    private fun bindingRecycler() {
        binding.recyclerMessages.layoutManager = LinearLayoutManager(context)
        binding.recyclerMessages.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(receiver: User): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, receiver.name)
                    putString(USERID, receiver.id)
                }
            }
        }
    }

}