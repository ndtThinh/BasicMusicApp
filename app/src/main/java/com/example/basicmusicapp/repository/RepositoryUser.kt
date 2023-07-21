package com.example.basicmusicapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.basicmusicapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositoryUser {
    private var fireStore: FirebaseFirestore? = null
    private var userLiveData: MutableLiveData<ArrayList<User>>? = null
    fun getUserLiveData(): MutableLiveData<ArrayList<User>>? {
        return userLiveData
    }

    companion object {
        var isExits: Boolean = false
    }

    constructor() {
        fireStore = Firebase.firestore
    }

    interface OnLoginSigUpListener {
        fun onExits(exits: Boolean)
        fun onLogin(confirm: Boolean)
    }

    fun register(
        userName: String,
        password: String,
        email: String,
        exitsListener: OnLoginSigUpListener
    ) {
        var newUserId: Long = 1
        fireStore!!.collection("User").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (item in it) {
                    if (userName == item.getString("userName")) {
                        isExits = true
                        exitsListener.onExits(true)
                        Log.d("Register", "User name is existed")
                    }
                }
                if (!isExits) {
                    for (item in it) {
                        var userId = item.getLong("userId")
                        if (userId != null) {
                            if (userId > newUserId)
                                newUserId = userId
                        }
                    }
                    newUserId++
                    val data = hashMapOf(
                        "userName" to userName,
                        "passWord" to password,
                        "email" to email,
                        "userId" to newUserId,
                        "fileImage" to ""
                    )
                    fireStore!!.collection("User").document(userName).set(data)
                        .addOnSuccessListener {
                            exitsListener.onExits(false)
                            isExits = false
                            Log.d("Register", "successfully")
                        }.addOnFailureListener {
                            Log.d("Register", "fail to register")
                        }
                }

            } else {
                Log.d("Register", "Khong thay user nao")
            }
        }
    }

    fun login(userName: String, password: String,onLoginSigUpListener: OnLoginSigUpListener) {
        fireStore!!.collection("User").get()
            .addOnSuccessListener {
                if(!it.isEmpty){
                    var isConfirm=false
                    for (item in it){
                        var mUserName=item.getString("userName")
                        var mPassword=item.getString("passWord")
                        if(userName==mUserName && password==mPassword){
                            isConfirm=true
                            break
                        }
                    }
                    if(isConfirm){
                        onLoginSigUpListener.onLogin(true)
                    }
                    else{
                        onLoginSigUpListener.onLogin(false)
                    }
                }
            }.addOnFailureListener {

            }
    }
}