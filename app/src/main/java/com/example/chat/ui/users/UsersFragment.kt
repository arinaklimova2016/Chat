package com.example.chat.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.databinding.FragmentUsersBinding
import com.example.chat.model.User
import com.example.chat.ui.chat.ChatFragment
import com.example.chat.ui.login.LoginFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding

    private val model by viewModel<UsersViewModel>()

    private val adapter by lazy {
        UsersAdapter(
            onUsersClicked = {
                onUsersClicked(it)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingRecycler()
        observeToViewModel()
        model.getUsers()
    }

    private fun bindingRecycler() {
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = adapter
    }

    private fun onUsersClicked(user: User) {
        val fragment = ChatFragment.newInstance(user)
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(
            R.id.fragment_container,
            fragment
        )
            ?.addToBackStack("")
            ?.commit()
    }

    private fun observeToViewModel() {
        model.users.observe(viewLifecycleOwner, { t ->
            adapter.submitList(t ?: listOf())
        })
        model.errorServer.observe(viewLifecycleOwner, {
            Toast.makeText(
                activity?.applicationContext,
                "Соединение с сервером потеряно",
                Toast.LENGTH_SHORT
            ).show()
            createLoginFragment()
            onDestroy()
        })
    }

    private fun createLoginFragment() {
        val fragment = LoginFragment()
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("")
            ?.commit()
    }

}