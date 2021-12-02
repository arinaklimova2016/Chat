package com.example.chat.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.ui.chat.ChatFragment
import com.example.chat.databinding.FragmentUsersBinding
import com.example.chat.model.User
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
        val userName = user.name
        val userId = user.id
        val fragment = ChatFragment.newInstance(userName, userId)
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

    }

}