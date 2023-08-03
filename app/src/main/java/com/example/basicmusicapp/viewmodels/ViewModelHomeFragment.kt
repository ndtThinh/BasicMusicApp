package com.example.basicmusicapp.viewmodels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.repository.RepositoryAudio
import com.example.basicmusicapp.repository.RepositoryImage
import com.example.basicmusicapp.repository.RepositorySong

class ViewModelHomeFragment : ViewModel() {
    private var listSongLiveData = MutableLiveData<ArrayList<SongMusic>>()
    private var mRepositoryImage = RepositoryImage()
    private var mRepositoryAudio = RepositoryAudio()
    private var mRepositorySong = RepositorySong()

    //live data
    var liveDataSongLove = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongSad = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongChina = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongRap = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongRemix = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongChill = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongBeat = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongFun = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongRedMusic = MutableLiveData<ArrayList<SongMusic>>()
    var liveDataSongEuro = MutableLiveData<ArrayList<SongMusic>>()


    fun loadSong() {
        mRepositorySong.getDataSong()
        mRepositorySong.liveDataSongLove.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongLove.value = it
            }
        }
        mRepositorySong.liveDataSongSad.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongSad.value = it
            }
        }
        mRepositorySong.liveDataSongChina.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongChina.value = it
            }
        }
        mRepositorySong.liveDataSongRap.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongRap.value = it
            }
        }
        mRepositorySong.liveDataSongRemix.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongRemix.value = it
            }
        }
        mRepositorySong.liveDataSongChill.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongChill.value = it
            }
        }
        mRepositorySong.liveDataSongBeat.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongBeat.value = it
            }
        }
        mRepositorySong.liveDataSongFun.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongFun.value = it
            }
        }
        mRepositorySong.liveDataSongRedMusic.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongRedMusic.value = it
            }
        }
        mRepositorySong.liveDataSongEuro.observeForever {
            if (it.isNotEmpty()) {
                liveDataSongEuro.value = it
            }
        }
    }


}