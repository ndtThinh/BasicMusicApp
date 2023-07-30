package com.example.basicmusicapp.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RepositoryAudio {
    private var storeReference: StorageReference? = null

    constructor() {
        storeReference = FirebaseStorage.getInstance().reference
    }

    interface OnUploadListener{
        fun onUpLoad(boolean: Boolean)
    }
    interface OnGetAudioListener{
        fun onGetAudio(boolean: Boolean)
    }
    fun uploadAudio(audioUri: Uri,nameAudio:String,onUploadListener: OnUploadListener){
        storeReference!!.child("audios/$nameAudio").putFile(audioUri).addOnSuccessListener {
            onUploadListener.onUpLoad(true)
            Log.d("Upload", "upload audio successfully")
        }.addOnFailureListener {
            onUploadListener.onUpLoad(false)
            Log.d("Upload", "upload audio failed")
        }
    }
    fun readAudio(nameAudio: String,onGetAudioListener: OnGetAudioListener){
//        storeReference!!.child("audios/$nameAudio").get
    }
}