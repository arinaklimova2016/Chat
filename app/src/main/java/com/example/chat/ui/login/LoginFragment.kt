package com.example.chat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.chat.R
import com.example.chat.databinding.FragmentLoginBinding
import com.example.chat.ui.users.UsersFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val model by viewModel<LoginViewModel>()
    private lateinit var btnLogIn: Button
    private lateinit var editEmail: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogIn = binding.logIn
        editEmail = binding.editEmail
        progressBar = binding.progressBar


        btnLogIn.setOnClickListener {
            progressBar.visibility = ProgressBar.VISIBLE
            val name: String = editEmail.text.toString()
            observeToViewModel()
            model.getIp(name)
        }
    }

    private fun createFragment() {
        val fragment = UsersFragment()
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(
            R.id.fragment_container,
            fragment
        )
            ?.addToBackStack("")
            ?.commit()
    }

    private fun observeToViewModel() {
        model.idSingleLiveEvent.observe(this, Observer {
            println(it)
            createFragment()
        })
    }

}