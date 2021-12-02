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
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val model by viewModel<ChatViewModel>()

    private val itemUserName by lazy {
        requireArguments().getString(USERNAME)
    }

    private val itemUserId by lazy {
        requireArguments().getString(USERID)
    }

    private val adapter by lazy {
        ChatAdapter(

        )
    }

    private val txtView: TextView = binding.username
    private val editText: EditText = binding.editMessage
    private val btnSend: ImageView = binding.btnSendMessage

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

        txtView.text = itemUserName

        btnSend.setOnClickListener{
            val message: String = editText.text.toString()
            model.sendMessage(itemUserId!!, message)
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
        fun newInstance(user: String, userId: String): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, user)
                    putString(USERID, userId)
                }
            }
        }

    }

}