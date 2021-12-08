package com.example.chat.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

    //разобраться с вьюшками
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

        observeToViewModel()

        btnLogIn.setOnClickListener {
            progressBar.visibility = ProgressBar.VISIBLE
            val name: String = editEmail.text.toString()
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
        model.idSingleLiveEvent.observe(this, {
            createFragment()
        })
        model.errorSingleLiveEvent.observe(this, {
            progressBar.visibility = ProgressBar.INVISIBLE
            Toast.makeText(
                activity?.applicationContext,
                //в ресурсы строку
                "Не удалось подключится",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.finish()
    }

}


var toast: Toast? = null
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    toast?.cancel()
    toast = Toast.makeText(this, message, length)
    toast?.show()
}

fun Fragment.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, length)
}