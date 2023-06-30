package com.example.basicmusicapp.models

import java.io.Serializable

class Song(
    var title: String,
    var singer: String,
    var fileSong: Int,
    var image:Int
) : Serializable {

}