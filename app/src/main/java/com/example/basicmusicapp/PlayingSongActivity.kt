package com.example.basicmusicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.basicmusicapp.databinding.ActivityPlayingSongBinding
import com.example.basicmusicapp.models.SongMusic

class PlayingSongActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayingSongBinding
    private var listSong=ArrayList<SongMusic>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setData()
    }

    private fun setData() {
        val song: SongMusic = intent.getSerializableExtra("song") as SongMusic
        listSong = intent.getStringArrayListExtra("listSong") as ArrayList<SongMusic>
        val style=intent.getIntExtra("style",0) as Int
        if(style!=null){
            when(style){
                Constants.STYLE_LOVE -> binding.tvStyleSong.text="Nhạc lãng mạn"
                Constants.STYLE_SAD -> binding.tvStyleSong.text="Nhạc buồn"
                Constants.STYLE_RAP -> binding.tvStyleSong.text="Nhạc Rap"
                Constants.STYLE_REMIX -> binding.tvStyleSong.text="Nhạc Remix"
                Constants.STYLE_RED -> binding.tvStyleSong.text="Nhạc cách mạng"
                Constants.STYLE_CHINA -> binding.tvStyleSong.text="Nhạc Hoa"
                Constants.STYLE_EURO -> binding.tvStyleSong.text="Nhạc Âu Mỹ"
                Constants.STYLE_BEAT -> binding.tvStyleSong.text="Nhạc Không Lời"
                Constants.STYLE_FUN -> binding.tvStyleSong.text="Nhạc tươi vui"
                Constants.STYLE_LOFI -> binding.tvStyleSong.text="Nhạc Lofi Chill"
                0 ->binding.tvStyleSong.text="World of Music"
            }
        }
        if (song != null) {
            binding.apply {
                TvNameSongPlay.text = song.nameSong
                TvNameSingerPlay.text = song.nameSinger
                Log.d("NewActivity", "size: ${listSong.size}")
            }
        }

    }
}