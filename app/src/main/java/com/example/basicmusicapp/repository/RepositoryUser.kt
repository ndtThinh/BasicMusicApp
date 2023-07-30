package com.example.basicmusicapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.basicmusicapp.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositoryUser {
    private var fireStore: FirebaseFirestore? = null
    private var userCurrentLiveData = MutableLiveData<ArrayList<User>>()
    fun getUserCurrentLiveData(): MutableLiveData<ArrayList<User>>? {
        return userCurrentLiveData
    }

    companion object {
        var isExits: Boolean = false
    }

    constructor() {
        fireStore = Firebase.firestore
    }

    interface OnLoginSigUpListener {
        fun onExits(exits: Boolean)
        fun onLogin(confirm: Boolean, userId: Long)
    }

    interface OnUpdateUserListener {
        fun onUpdateUser(boolean: Boolean)
    }

    fun register(
        userName: String,
        password: String,
        email: String,
        singerName: String?,
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
                    var newSingerId = 0
                    if (singerName == "") {
                        newSingerId = 0
                    } else {
                        for (item in it) {
                            var singerId = item.getLong("singerId")
                            if (singerId != null) {
                                if (singerId > newUserId)
                                    newUserId = singerId
                            }
                        }
                        newSingerId++
                    }
                    val data = hashMapOf(
                        "userName" to userName,
                        "passWord" to password,
                        "email" to email,
                        "userId" to newUserId,
                        "fileImage" to "",
                        "singerName" to singerName,
                        "singerId" to newSingerId
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

    fun login(userName: String, password: String, onLoginSigUpListener: OnLoginSigUpListener) {
        fireStore!!.collection("User").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    var userIdConfirm: Long = -1
                    var isConfirm = false
                    for (item in it) {
                        var mUserName = item.getString("userName")
                        var mPassword = item.getString("passWord")
                        userIdConfirm = item.getLong("userId")!!
                        if (userName == mUserName && password == mPassword) {
                            isConfirm = true
                            break
                        }
                    }
                    if (isConfirm) {
                        onLoginSigUpListener.onLogin(true, userIdConfirm)
                    } else {
                        onLoginSigUpListener.onLogin(false, userIdConfirm)
                    }
                }
            }.addOnFailureListener {

            }
    }

    fun getUserCurrent(userId: Long) {
        var listUser = ArrayList<User>()
        fireStore?.collection("User")?.whereEqualTo("userId", userId)
            ?.get()?.addOnSuccessListener {
                if (!it.isEmpty) {
                    for (item in it) {
                        val userName = item.getString("userName").toString()
                        val password = item.getString("passWord").toString()
                        val email = item.getString("email").toString()
                        val userId = item.getLong("userId") as Long
                        val fileImage = item.getString("fileImage").toString()
                        val singerName = item.getString("singerName").toString()
                        val singerId = item.getLong("singerId") as Long
                        var userCurrent =
                            User(userName, password, email, userId, fileImage, singerName, singerId)
                        listUser.add(userCurrent)
                        Log.d("Account", "User Repository: $userName$userId")
                    }
                    userCurrentLiveData?.value = listUser
                    Log.d("Account", "User Repository: ${userCurrentLiveData?.value!!.size}")
                }
            }?.addOnFailureListener {
                Log.d("account : ", "Not found any user")
                userCurrentLiveData?.value = ArrayList()
            }
    }

    fun checkLogged(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("userId", -1)
    }

    fun updateUser(user: User, onUpdateUserListener: OnUpdateUserListener) {
        val dataUpdate = hashMapOf(
            "userName" to user.userName,
            "passWord" to user.passWord,
            "email" to user.email,
            "userId" to user.userId,
            "fileImage" to user.userName,
            "singerName" to user.singerName,
            "singerId" to user.singerId
        )
        fireStore?.collection("User")?.document(user.userName)
            ?.update(dataUpdate as Map<String, Any>)
            ?.addOnSuccessListener {
                onUpdateUserListener.onUpdateUser(true)
                Log.d("Update", "update user successfully")
            }?.addOnFailureListener {
                onUpdateUserListener.onUpdateUser(false)
                Log.d("Update", "update image failed")
            }
    }

    fun keepLoginInUser(userId: Long, context: Context) {
        val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("userId", userId)
        editor.apply()
    }

    fun logOutUser(context: Context) {
        val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userId")
        editor.apply()
    }
}