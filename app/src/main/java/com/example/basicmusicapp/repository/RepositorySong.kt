package com.example.basicmusicapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.models.SongMusic
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositorySong {
    private var fireStore: FirebaseFirestore? = null
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
    private var listLiveDataSong = arrayOf(
        liveDataSongLove, liveDataSongSad, liveDataSongChina, liveDataSongRap, liveDataSongRemix,
        liveDataSongChill, liveDataSongBeat, liveDataSongFun, liveDataSongRedMusic, liveDataSongEuro
    )

    constructor() {
        fireStore = Firebase.firestore
    }

    interface OnUploadSongListener {
        fun onUpload(boolean: Boolean)
    }

    fun upLoadSong(
        nameSong: String,
        nameSinger: String,
        listStyles: List<Int>,
        singerId: Long,
        imageUrl: String,
        audioUrl: String,
        onUploadSongListener: OnUploadSongListener
    ) {
        var newSongId: Long = 1
        fireStore!!.collection("Song").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (item in it) {
                    val songId = item.getLong("songId")
                    if (songId!! > newSongId) {
                        newSongId = songId
                    }
                }
                newSongId++
                val data = hashMapOf(
                    "songId" to newSongId,
                    "nameSong" to nameSong,
                    "singerId" to singerId,
                    "nameSinger" to nameSinger,
                    "styles" to listStyles,
                    "fileSong" to audioUrl,
                    "imageSong" to imageUrl
                )
                fireStore!!.collection("Song").document(nameSong).set(data)
                    .addOnSuccessListener {
                        onUploadSongListener.onUpload(true)
                        Log.d("Upload", "Upload song successfully")
                    }.addOnFailureListener {
                        onUploadSongListener.onUpload(false)
                        Log.d("Upload", "fail to upload song")
                    }
            } else {
                val data = hashMapOf(
                    "songId" to newSongId,
                    "nameSong" to nameSong,
                    "singerId" to singerId,
                    "nameSinger" to nameSinger,
                    "styles" to listStyles,
                    "fileSong" to nameSong,
                    "imageSong" to nameSong
                )
                fireStore!!.collection("Song").document(nameSong).set(data)
                    .addOnSuccessListener {
                        onUploadSongListener.onUpload(true)
                        Log.d("Upload", "Upload song successfully")
                    }.addOnFailureListener {
                        onUploadSongListener.onUpload(false)
                        Log.d("Upload", "fail to upload song")
                    }
            }
        }
    }

    fun getDataSong() {
        fireStore!!.collection("Song").get().addOnSuccessListener {
            if (!it.isEmpty) {
                var songLove = ArrayList<SongMusic>()
                var songSad = ArrayList<SongMusic>()
                var songChina = ArrayList<SongMusic>()
                var songEuro = ArrayList<SongMusic>()
                var songRap = ArrayList<SongMusic>()
                var songRemix = ArrayList<SongMusic>()
                var songFun = ArrayList<SongMusic>()
                var songChill = ArrayList<SongMusic>()
                var songRedMusic = ArrayList<SongMusic>()
                var songBeat = ArrayList<SongMusic>()
                var listStyleSong = arrayOf(
                    songLove, songSad, songChina,
                    songRap, songRemix, songChill, songBeat,
                    songFun, songRedMusic, songEuro
                )
                for (item in it) {
                    val nameSong = item.getString("nameSong").toString()
                    val songId: Long = item.getLong("songId").toString().toLong()
                    val singerId: Long = item.getLong("singerId").toString().toLong()
                    val imageSong = item.getString("imageSong").toString()
                    val fileSong = item.getString("fileSong").toString()
                    val singerName = item.getString("nameSinger").toString()
                    val styles: List<Int> = item.get("styles") as List<Int>
                    val songMusic = SongMusic(
                        songId, nameSong, singerName, singerId, styles, imageSong, fileSong
                    )
                    Log.d("Get Data Song: ", "${songMusic.nameSong}" + " ${songMusic.styles}")
                    for(i in 0 until songMusic.styles.size){
                        when(songMusic.styles[i]){
                            Constants.STYLE_LOVE -> songLove.add(songMusic)
                            Constants.STYLE_SAD -> songSad.add(songMusic)
                            Constants.STYLE_CHINA -> songChina.add(songMusic)
                            Constants.STYLE_RAP -> songRap.add(songMusic)
                            Constants.STYLE_REMIX -> songRemix.add(songMusic)
                            Constants.STYLE_LOFI -> songChill.add(songMusic)
                            Constants.STYLE_BEAT -> songBeat.add(songMusic)
                            Constants.STYLE_FUN -> songFun.add(songMusic)
                            Constants.STYLE_EURO -> songEuro.add(songMusic)
                            Constants.STYLE_RED -> songRedMusic.add(songMusic)
                        }
                    }
                    Log.d("GetData song:", " ${songLove.size}")
                }
                for (i in 0 until 10) {
                    listLiveDataSong[i].value = listStyleSong[i]
                }
            }

        }.addOnFailureListener {
            Log.d("GetData Repository song", "Not found any song")
        }
    }
}