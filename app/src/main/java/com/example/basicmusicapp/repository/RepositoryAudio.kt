package com.example.basicmusicapp.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class RepositoryAudio {
    private var storeReference: StorageReference? = null
    var audioUrl:String=""

    constructor() {
        storeReference = FirebaseStorage.getInstance().reference
    }

    interface OnUploadListener {
        fun onUpLoad(boolean: Boolean)
    }

    interface OnGetAudioListener {
        fun onGetAudio(boolean: Boolean)
    }

    fun uploadAudio(audioUri: Uri, nameAudio: String, onUploadListener: OnUploadListener) {
        storeReference!!.child("audios/$nameAudio").putFile(audioUri).addOnSuccessListener {
            onUploadListener.onUpLoad(true)
            Log.d("Upload", "upload audio successfully")
        }.addOnFailureListener {
            onUploadListener.onUpLoad(false)
            Log.d("Upload", "upload audio failed")
        }
    }

    fun getUrlAudio(nameAudio: String,onGetAudioListener: OnGetAudioListener) {
        storeReference!!.child("audios/$nameAudio").downloadUrl.addOnSuccessListener {
            audioUrl=it.toString()
            onGetAudioListener.onGetAudio(true)
            Log.d("Get Url:", "Get Audio URL successfully")
        }.addOnFailureListener {
            onGetAudioListener.onGetAudio(false)
            Log.d("Get Url:", "Get Audio URL successfully")
        }
    }
}