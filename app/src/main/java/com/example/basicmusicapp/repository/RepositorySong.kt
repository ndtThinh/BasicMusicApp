package com.example.basicmusicapp.repository

import android.util.Log
import com.example.basicmusicapp.models.SongMusic
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RepositorySong {
    private var fireStore: FirebaseFirestore? = null

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
        onUploadSongListener: OnUploadSongListener
    ) {
        var newSongId: Long = 1
        fireStore!!.collection("Song").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (item in it) {
                    var songId = item.getLong("songId")
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
}