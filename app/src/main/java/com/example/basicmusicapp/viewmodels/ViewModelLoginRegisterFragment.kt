package com.example.basicmusicapp.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.models.User
import com.example.basicmusicapp.repository.RepositoryUser


class ViewModelLoginRegisterFragment : ViewModel() {

    var listUser: MutableLiveData<ArrayList<User>>? = null
    var mRepositoryUser = RepositoryUser()


    fun registerViewModel(
        userName: String,
        password: String,
        email: String,
        exitsListener: RepositoryUser.OnLoginSigUpListener
    ) {
        mRepositoryUser.register(
            userName,
            password,
            email,
            object : RepositoryUser.OnLoginSigUpListener {
                override fun onExits(exits: Boolean) {
                    exitsListener.onExits(exits)
                }

                override fun onLogin(confirm: Boolean, userId: Long) {

                }
            })
    }

    fun login(
        userName: String,
        password: String,
        loginSigUpListener: RepositoryUser.OnLoginSigUpListener
    ) {
        mRepositoryUser.login(userName, password, object : RepositoryUser.OnLoginSigUpListener {
            override fun onExits(exits: Boolean) {
            }

            override fun onLogin(confirm: Boolean, userId: Long) {
                loginSigUpListener.onLogin(confirm,userId)
            }

        })
    }

    fun keepLoginUser(userId: Long, context: Context) {
        mRepositoryUser.keepLoginInUser(userId, context)
    }

}