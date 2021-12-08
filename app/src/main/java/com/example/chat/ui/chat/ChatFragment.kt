package com.example.chat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.constants.Constants.USER
import com.example.chat.databinding.FragmentChatBinding
import com.example.chat.model.User
import com.example.chat.ui.login.LoginFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val model by viewModel<ChatViewModel>(parameters = { parametersOf(itemUser) })

    private val itemUser: User by lazy {
        requireArguments().getParcelable(USER)!!
    }

    private val adapter by lazy {
        ChatAdapter(
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
        ////////////
        with(binding) {
            username.text = itemUser.name
            btnSend.setOnClickListener {

            }
        }

        txtView.text = itemUser.name
        btnSend.setOnClickListener {
            val message: String = editText.text.toString()
            model.sendMessage(message)
            editText.text.clear()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        model.messages.observe(viewLifecycleOwner, {
            adapter.submitList(it ?: listOf())
        })
        model.errorServer.observe(viewLifecycleOwner, {
            Toast.makeText(
                activity?.applicationContext,
                "Соединение с сервером потеряно",
                Toast.LENGTH_SHORT
            ).show()
            createLoginFragment()
        })
    }

    private fun createLoginFragment() {
        activity?.supportFragmentManager?.popBackStack("", 0)
        val fragment = LoginFragment()
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("")
            ?.commit()
    }

    private fun bindingRecycler() {
        binding.recyclerMessages.layoutManager = LinearLayoutManager(context)
        binding.recyclerMessages.adapter = adapter
    }

    companion object {
        fun newInstance(receiver: User): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER, receiver)
                }
            }
        }
    }

}