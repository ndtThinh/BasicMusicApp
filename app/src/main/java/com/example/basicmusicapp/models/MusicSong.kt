package com.example.basicmusicapp.models

class MusicSong(
    var songId: Long,
    var nameSong: String,
    var singer: String,
    var singerId: Long,
    var styles: Array<Long>,
    var imageSong: String,
    var fileSong: String
) {
}