package com.example.basicmusicapp.viewmodels

import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.PlayingSongActivity
import com.example.basicmusicapp.models.SongMusic

class ViewModelNowSongPlaying : ViewModel() {
    var liveDataSong = MutableLiveData<SongMusic>()
    var liveDataMedia = MutableLiveData<MediaPlayer>()

    fun loadSong() {
        PlayingSongActivity.songService!!.liveDataCurrentSong.observeForever {
            if (it != null) {
                liveDataSong.value = it
            }
        }
    }
    fun loadMediaPlayer(){
        PlayingSongActivity.songService!!.liveDataMediaPlayer.observeForever {
            if(it!=null){
                liveDataMedia.value=it
            }
        }
    }
}