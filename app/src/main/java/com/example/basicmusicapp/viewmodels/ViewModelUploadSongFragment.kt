package com.example.basicmusicapp.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.repository.RepositoryImage
import com.example.basicmusicapp.repository.RepositoryAudio
import com.example.basicmusicapp.repository.RepositorySong

class ViewModelUploadSongFragment : ViewModel() {
    private var mRepositoryImage = RepositoryImage()
    private var mRepositoryAudio = RepositoryAudio()
    private var mRepositorySong = RepositorySong()
    private var newSongMusic: SongMusic? = null

    interface OnUploadSongViewModelLis {
        fun onUpload(boolean: Boolean)
    }

    fun upLoadSongViewModel(
        nameSong: String,
        nameSinger: String,
        listStyles: List<Int>,
        singerId: Long,
        imageUri: Uri,
        audioUri: Uri,
        onUploadSongViewModelLis: OnUploadSongViewModelLis
    ) {
        var imageUrl: String = ""
        var audioUrl: String = ""
        mRepositoryImage.upLoadImage(imageUri, nameSong, object : RepositoryImage.OnUpLoadListener {
            override fun onUpLoad(boolean: Boolean) {
                if (boolean) {
                    mRepositoryImage.getUrlImage(nameSong,
                        object : RepositoryImage.OnGetImageListener {
                            override fun onGetImage(boolean: Boolean) {
                                if (boolean) {
                                    imageUrl = mRepositoryImage.urlImage
                                    mRepositoryAudio.uploadAudio(
                                        audioUri,
                                        nameSong,
                                        object : RepositoryAudio.OnUploadListener {
                                            override fun onUpLoad(boolean: Boolean) {
                                                if (boolean) {
                                                    mRepositoryAudio.getUrlAudio(nameSong,
                                                        object :
                                                            RepositoryAudio.OnGetAudioListener {
                                                            override fun onGetAudio(boolean: Boolean) {
                                                                audioUrl = mRepositoryAudio.audioUrl
                                                                mRepositorySong.upLoadSong(
                                                                    nameSong,
                                                                    nameSinger,
                                                                    listStyles,
                                                                    singerId,
                                                                    imageUrl,
                                                                    audioUrl,
                                                                    object :
                                                                        RepositorySong.OnUploadSongListener {
                                                                        override fun onUpload(
                                                                            boolean: Boolean
                                                                        ) {
                                                                            onUploadSongViewModelLis.onUpload(
                                                                                true
                                                                            )
                                                                        }
                                                                    })
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
        })
    }
//    fun upLoadSongViewModel(nameSong:String,nameSinger:String,listStyles:List<Int>,singerId:Long,imageUri: Uri,audioUri: Uri,onUploadSongViewModelLis: OnUploadSongViewModelLis){
//        mRepositoryImage.upLoadImage(imageUri,nameSong,object :RepositoryImage.OnUpLoadListener{
//            override fun onUpLoad(boolean: Boolean) {
//                if(boolean){
//                    mRepositoryAudio.uploadAudio(audioUri,nameSong,object :RepositoryAudio.OnUploadListener{
//                        override fun onUpLoad(boolean: Boolean) {
//                          if(boolean){
//                              mRepositorySong.upLoadSong(nameSong,nameSinger,listStyles,singerId,object :RepositorySong.OnUploadSongListener{
//                                  override fun onUpload(boolean: Boolean) {
//                                      if(boolean){
//                                            onUploadSongViewModelLis.onUpload(true)
//                                      }
//                                  }
//                              })
//                          }
//                        }
//                    })
//                }
//            }
//        })
//    }
}