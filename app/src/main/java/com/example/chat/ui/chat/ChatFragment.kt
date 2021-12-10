package com.example.chat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.databinding.FragmentChatBinding
import com.example.domain.model.User
import com.example.chat.ui.login.LoginFragment
import com.example.chat.utils.Constants.TOAST_TXT_CONNECTION_LOST
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val model by viewModel<ChatViewModel>(parameters = { parametersOf(itemUser) })

    private val itemUser: User by lazy {
        requireArguments().getParcelable(USERRECEIVER)!!
    }

    private val adapter by lazy {
        ChatAdapter(
            receiver = itemUser,
            you = model.getYou()
        )
    }

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

        with(binding) {
            username.text = itemUser.name
            btnSendMessage.setOnClickListener {
                val message: String = editMessage.text.toString()
                model.sendMessage(message)
                editMessage.text.clear()
            }
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
                TOAST_TXT_CONNECTION_LOST,
                Toast.LENGTH_SHORT
            ).show()
            transitionToTheFirstWindow()
        })
    }

    private fun transitionToTheFirstWindow() {
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
        private const val USERRECEIVER = "user"

        fun newInstance(receiver: User): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USERRECEIVER, receiver)
                }
            }
        }
    }
}