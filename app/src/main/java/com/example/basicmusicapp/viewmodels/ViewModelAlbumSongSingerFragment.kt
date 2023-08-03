package com.example.basicmusicapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.models.User
import com.example.basicmusicapp.repository.RepositoryUser

class ViewModelAlbumSongSingerFragment : ViewModel() {
    var liveDataUserSinger = MutableLiveData<ArrayList<User>>()
    private var mRepositoryUser = RepositoryUser()

    fun getAllUser() {
        mRepositoryUser.getAllSinger()
        mRepositoryUser.userSingerLiveData.observeForever {
            if (it.isNotEmpty()) {
                liveDataUserSinger.value = it
            }
        }
    }
}