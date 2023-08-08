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

    fun newUpdateUser(
        userName: String,
        password: String,
        email: String,
        userId: Long,
        imageUri: Uri?,
        imageUrl:String,
        singerName: String,
        singerId: Long,
        onUpDateUserListenerVm: OnUpDateUserListenerVm
    ) {
        if(imageUrl!=""){
            if(imageUri!=null){
                mRepositoryImage.deleteImage(userName,object :RepositoryImage.OnDeleteListener{
                    override fun onDeleteImage(boolean: Boolean) {
                        if(boolean){
                            mRepositoryImage.upLoadImage(imageUri,userName,object :RepositoryImage.OnUpLoadListener{
                                override fun onUpLoad(boolean: Boolean) {
                                    if(boolean){
                                        mRepositoryImage.getUrlImage(userName,object :RepositoryImage.OnGetImageListener{
                                            override fun onGetImage(boolean: Boolean) {
                                                if(boolean){
                                                    var newImageUrl=mRepositoryImage.urlImage
                                                    val userUpdate=User(userName,password,email,userId,newImageUrl,singerName,singerId)
                                                    mRepositoryUser.updateUser(userUpdate,object :RepositoryUser.OnUpdateUserListener{
                                                        override fun onUpdateUser(boolean: Boolean) {
                                                            onUpDateUserListenerVm.onUpdateViewModel(boolean)
                                                        }
                                                    })
                                                }else{
                                                    onUpDateUserListenerVm.onUpdateViewModel(false)
                                                }
                                            }
                                        })
                                    }else{
                                        onUpDateUserListenerVm.onUpdateViewModel(false)
                                    }
                                }
                            })
                        }else{
                            onUpDateUserListenerVm.onUpdateViewModel(false)
                        }
                    }
                })
            }
            else{
                val userUpdate=User(userName,password,email,userId,imageUrl,singerName,singerId)
                mRepositoryUser.updateUser(userUpdate,object :RepositoryUser.OnUpdateUserListener{
                    override fun onUpdateUser(boolean: Boolean) {
                        onUpDateUserListenerVm.onUpdateViewModel(boolean)
                    }
                })
            }
        }else{
            if(imageUri==null){
                val userUpdate=User(userName,password,email,userId,imageUrl,singerName,singerId)
                mRepositoryUser.updateUser(userUpdate,object :RepositoryUser.OnUpdateUserListener{
                    override fun onUpdateUser(boolean: Boolean) {
                        onUpDateUserListenerVm.onUpdateViewModel(boolean)
                    }
                })
            }
            else{
                mRepositoryImage.upLoadImage(imageUri,userName,object :RepositoryImage.OnUpLoadListener{
                    override fun onUpLoad(boolean: Boolean) {
                        if(boolean){
                            mRepositoryImage.getUrlImage(userName,object :RepositoryImage.OnGetImageListener{
                                override fun onGetImage(boolean: Boolean) {
                                    if(boolean){
                                        var newImageUrl=mRepositoryImage.urlImage
                                        val userUpdate=User(userName,password,email,userId,newImageUrl,singerName,singerId)
                                        mRepositoryUser.updateUser(userUpdate,object :RepositoryUser.OnUpdateUserListener{
                                            override fun onUpdateUser(boolean: Boolean) {
                                                onUpDateUserListenerVm.onUpdateViewModel(boolean)
                                            }
                                        })
                                    }
                                }
                            })
                        }
                    }
                })
            }
        }
    }

}