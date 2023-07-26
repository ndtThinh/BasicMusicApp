package com.example.basicmusicapp.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.example.basicmusicapp.models.User
import com.example.basicmusicapp.repository.RepositoryImage
import com.example.basicmusicapp.repository.RepositoryUser

class ViewModelAccountFragment : ViewModel() {
    private var userCurrent = MutableLiveData<ArrayList<User>>()
    private var mRepositoryUser = RepositoryUser()
    private var mRepositoryImage = RepositoryImage()

    fun getListUser(userId: Long): MutableLiveData<ArrayList<User>>? {
        loadUser(userId)
        return userCurrent
    }

    private fun loadUser(userId: Long) {
        mRepositoryUser.getUserCurrent(userId)
        mRepositoryUser.getUserCurrentLiveData()?.observeForever { t ->
            if (t.isNotEmpty() && t != null) {
                userCurrent?.value = t
                Log.d("Account", "lisUser:" + userCurrent!!.value?.size.toString())
            }
        }
    }

    interface OnUpDateUserListenerVm {
        fun onUpdateViewModel(boolean: Boolean)
    }

    fun updateUser(
        user: User,
        imageUri: Uri?,
        isHasImage: Boolean,
        onUpDateUserListenerVm: OnUpDateUserListenerVm
    ) {
        if (isHasImage) {
            if (imageUri != null) {
                mRepositoryImage.deleteImage(
                    user.fileImage,
                    object : RepositoryImage.OnDeleteListener {
                        override fun onDeleteImage(boolean: Boolean) {
                            if (boolean) {
                                mRepositoryImage.upLoadImage(
                                    imageUri,
                                    user.userName,
                                    object : RepositoryImage.OnUpLoadListener {
                                        override fun onUpLoad(boolean: Boolean) {
                                            if (boolean) {
                                                mRepositoryUser.updateUser(user,
                                                    object : RepositoryUser.OnUpdateUserListener {
                                                        override fun onUpdateUser(boolean: Boolean) {
                                                            onUpDateUserListenerVm.onUpdateViewModel(
                                                                boolean
                                                            )
                                                        }
                                                    })
                                            }
                                        }
                                    })
                            }
                        }
                    })
            }else{
                mRepositoryUser.updateUser(user,
                    object : RepositoryUser.OnUpdateUserListener {
                        override fun onUpdateUser(boolean: Boolean) {
                            onUpDateUserListenerVm.onUpdateViewModel(boolean)
                        }
                    })
            }
        } else {
            if (imageUri == null) {
                mRepositoryUser.updateUser(user,
                    object : RepositoryUser.OnUpdateUserListener {
                        override fun onUpdateUser(boolean: Boolean) {
                            onUpDateUserListenerVm.onUpdateViewModel(boolean)
                        }
                    })
            } else {
                mRepositoryImage.upLoadImage(
                    imageUri,
                    user.userName,
                    object : RepositoryImage.OnUpLoadListener {
                        override fun onUpLoad(boolean: Boolean) {
                            if (boolean) {
                                mRepositoryUser.updateUser(user,
                                    object : RepositoryUser.OnUpdateUserListener {
                                        override fun onUpdateUser(boolean: Boolean) {
                                            onUpDateUserListenerVm.onUpdateViewModel(
                                                boolean
                                            )
                                        }
                                    })
                            }
                        }
                    })
            }
        }
    }
}