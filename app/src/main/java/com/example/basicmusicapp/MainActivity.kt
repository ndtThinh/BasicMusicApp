package com.example.basicmusicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.basicmusicapp.databinding.ActivityMainBinding
import com.example.basicmusicapp.fragments.*
import com.example.basicmusicapp.service.SongService

class MainActivity : AppCompatActivity(), LoginFragment.OnChangScreen {
    private lateinit var binding: ActivityMainBinding
    private var userId: Long? = null

    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        userId = intent.getLongExtra("UserId", -1)
        Log.d("Login", userId.toString())
        if (userId!! < 0) {
            binding.bottomNavigation.visibility = View.GONE
            changeFragment(LoginFragment())
        } else {
            changeFragment(HomeFragment())
            binding.bottomNavigation.visibility = View.VISIBLE
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeNav -> changeFragment(HomeFragment())
                R.id.accountNav -> changeFragment(AccountFragment(userId!!))
                R.id.albumNav -> changeFragment(AlbumSingerFragment())
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        val intent = Intent(this, SongService::class.java)
        stopService(intent)
//        val intent2 = Intent(this, MusicService::class.java)
//        stopService(intent2)
//        disconnectFromService()
    }

    override fun onChanged(id: Long?) {
        binding.bottomNavigation.visibility = View.VISIBLE
        userId = id
    }

    override fun onResume() {
        super.onResume()
        if (PlayingSongActivity.isBound) {
            val fragmentNowSongPlaying = NowSongPlayingFragment()
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frameLayout_song_playing, fragmentNowSongPlaying)
            fragmentTransaction.commit()
            val bundle = Bundle()
            bundle.putSerializable("songNow", PlayingSongActivity.songService!!.currentSong)
            bundle.putInt("index", PlayingSongActivity.songService!!.mIndex)
            bundle.putSerializable("listSong", PlayingSongActivity.songService!!.listSongCurrent)
            fragmentNowSongPlaying.arguments = bundle
            binding.frameLayoutSongPlaying.visibility = View.VISIBLE
            Log.d(
                "SongCurrentMain:",
                PlayingSongActivity.songService!!.currentSong!!.nameSong.toString()
            )
        } else {
            binding.frameLayoutSongPlaying.visibility = View.GONE
        }

    }

}