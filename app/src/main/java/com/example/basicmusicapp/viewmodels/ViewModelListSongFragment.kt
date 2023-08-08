package com.example.basicmusicapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.models.User
import com.example.basicmusicapp.repository.RepositorySong
import com.example.basicmusicapp.repository.RepositoryUser

class ViewModelListSongFragment : ViewModel() {
    var listSongLiveData = MutableLiveData<ArrayList<SongMusic>>()
    var singerCurrentLiveData = MutableLiveData<User>()
    private var mRepositorySong = RepositorySong()
    private var mRepositoryUser = RepositoryUser()

    fun getDataSongBySingerId(singerId: Long) {
        mRepositorySong.getSongBySinger(singerId)
        mRepositorySong.liveDataSongOfSinger.observeForever {
            if (it.isNotEmpty()) {
                listSongLiveData.value = it
            }
        }
    }

    fun getDataSinger(singerId: Long) {
        mRepositoryUser.getSingerCurrent(singerId)
        mRepositoryUser.singerCurrentLiveData.observeForever {
            singerCurrentLiveData.value = it
        }
    }
}