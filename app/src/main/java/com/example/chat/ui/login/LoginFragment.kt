package com.example.chat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.chat.R
import com.example.chat.databinding.FragmentLoginBinding
import com.example.chat.ui.users.UsersFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val model by viewModel<LoginViewModel>()

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

        observeToViewModel()
        with(binding) {
            logIn.setOnClickListener {
                progressBar.visibility = ProgressBar.VISIBLE
                val name: String = editEmail.text.toString()
                model.getIp(name)
            }
        }
    }

    private fun transitionToTheNextWindow() {
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
        model.triggerNextNavigation.observe(this, {
            transitionToTheNextWindow()
        })
        model.errorServer.observe(this, {
            binding.progressBar.visibility = ProgressBar.INVISIBLE
            Toast.makeText(
                activity?.applicationContext,
                TOAST_TXT,
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    companion object {
        private const val TOAST_TXT = "Не удалось подключиться"
    }
}