package com.example.basicmusicapp.models

class SongMusic(
    var songId: Long,
    var nameSong: String,
    var nameSinger: String,
    var singerId: Long,
    var styles: List<Int>,
    var imageSong: String,
    var fileSong: String
) {
}