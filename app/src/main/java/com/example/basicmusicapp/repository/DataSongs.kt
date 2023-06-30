package com.example.basicmusicapp.repository

import com.example.basicmusicapp.R
import com.example.basicmusicapp.models.Song

public class DataSongs() {
    var song1 = Song("Chỉ vì quá hi vọng", "Hoài Lâm", R.raw.chiviquahivong,R.drawable.chiviquahivong)
    var song2 = Song("Đi qua mùa hạ", "Thái Đinh", R.raw.diquamuaha,R.drawable.diquamuaha)
    var song3 = Song("Đoạn kết mới", "Hoàng Dũng", R.raw.doanketmoi,R.drawable.doanketmoi)
    var song4 = Song("Không thể say", "Hiếu Thứ Hai", R.raw.khongthesay,R.drawable.khongthesay)
    var song5 = Song("Nắng ấm ngang qua", "Sơn Tùng MTP", R.raw.nangamngangqua,R.drawable.nangamngangqua)
    var song6 = Song("Nàng thơ", "Hoàng Dũng", R.raw.nangtho,R.drawable.nangtho)

    var listSongs = arrayListOf<Song>(song1, song2, song3, song4, song5, song6)

}