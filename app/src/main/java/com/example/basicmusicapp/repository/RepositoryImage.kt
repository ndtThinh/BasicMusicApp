package com.example.basicmusicapp.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class RepositoryImage {
    private var storeReference: StorageReference? = null
    var bitmap: Bitmap? = null

    constructor() {
        storeReference = FirebaseStorage.getInstance().reference
    }

    interface OnUpLoadListener {
        fun onUpLoad(boolean: Boolean)
    }

    interface OnGetImageListener {
        fun onGetImage(boolean: Boolean)
    }

    interface OnDeleteListener {
        fun onDeleteImage(boolean: Boolean)
    }

    fun upLoadImage(imageUri: Uri, imageName: String, onUpLoadListener: OnUpLoadListener) {
        storeReference!!.child("images/$imageName").putFile(imageUri).addOnSuccessListener {
            onUpLoadListener.onUpLoad(true)
            Log.d("Update", "upload image successfully")
        }.addOnFailureListener {
            onUpLoadListener.onUpLoad(false)
            Log.d("Update", "upload image failed")
            Log.d("Update :", it.toString())
        }
    }

    fun deleteImage(imageName: String, onDeleteListener: OnDeleteListener) {
        storeReference!!.child("images/$imageName").delete().addOnSuccessListener {
            onDeleteListener.onDeleteImage(true)
            Log.d("Update", "Delete image successfully")
        }.addOnFailureListener {
            onDeleteListener.onDeleteImage(false)
            Log.d("Update", "Delete image failed")
        }
    }

    fun readImage(imageName: String, onGetImageListener: OnGetImageListener) {
        val file: File = File.createTempFile("image", ".jpeg")
        storeReference!!.child("images/$imageName").getFile(file)
            .addOnSuccessListener {
                bitmap = BitmapFactory.decodeFile(file.absolutePath)
                onGetImageListener.onGetImage(true)
                Log.d("ReadUser", "get image successfully")
            }.addOnFailureListener {
                Log.d("ReadUser", "get image failed")
                onGetImageListener.onGetImage(false)
            }
    }
}