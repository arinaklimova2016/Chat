package com.example.chat.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.example.chat.R
import com.example.chat.databinding.FragmentLoginBinding
import com.example.chat.users.UsersFragment

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val model = LoginViewModel()

    private lateinit var btnLogIn: Button

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

        btnLogIn.setOnClickListener{
            val ip = model.getIp()
            createFragment()
        }



        observeToViewModel()


    }

    fun onClickByButton(){

    }

    private fun observeToViewModel() {

    }

    private fun createFragment() {
        val fragment = UsersFragment()
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack("")
            ?.commit()
    }

}