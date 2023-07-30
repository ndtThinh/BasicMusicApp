package com.example.basicmusicapp.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.repository.RepositoryImage
import com.example.basicmusicapp.repository.RepositoryAudio
import com.example.basicmusicapp.repository.RepositorySong

class ViewModelUploadSongFragment:ViewModel() {
    private var mRepositoryImage=RepositoryImage()
    private var mRepositoryAudio=RepositoryAudio()
    private var mRepositorySong=RepositorySong()

    interface OnUploadSongViewModelLis{
        fun onUpload(boolean: Boolean)
    }
    fun upLoadSongViewModel(nameSong:String,nameSinger:String,listStyles:List<Int>,singerId:Long,imageUri: Uri,audioUri: Uri,onUploadSongViewModelLis: OnUploadSongViewModelLis){
        mRepositoryImage.upLoadImage(imageUri,nameSong,object :RepositoryImage.OnUpLoadListener{
            override fun onUpLoad(boolean: Boolean) {
                if(boolean){
                    mRepositoryAudio.uploadAudio(audioUri,nameSong,object :RepositoryAudio.OnUploadListener{
                        override fun onUpLoad(boolean: Boolean) {
                          if(boolean){
                              mRepositorySong.upLoadSong(nameSong,nameSinger,listStyles,singerId,object :RepositorySong.OnUploadSongListener{
                                  override fun onUpload(boolean: Boolean) {
                                      if(boolean){
                                            onUploadSongViewModelLis.onUpload(true)
                                      }
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