package com.example.chat.login

import androidx.lifecycle.ViewModel
import com.example.chat.server.Udp
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    private val udp: Udp
) : ViewModel() {

    private val disposables = CompositeDisposable()

    fun getIp() {
        disposables.add(udp.getServerIp()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                }, {
                    printStackTrace()
                }
            )
        )
    }

}