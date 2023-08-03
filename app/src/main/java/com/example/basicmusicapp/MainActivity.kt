package com.example.basicmusicapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.basicmusicapp.databinding.ActivityMainBinding
import com.example.basicmusicapp.fragments.*
import com.example.basicmusicapp.service.MusicService

class MainActivity : AppCompatActivity(), LoginFragment.OnChangScreen {
    private lateinit var binding: ActivityMainBinding
    private var userId: Long? = null

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
                R.id.songsNav -> changeFragment(ListSongFragment())
                R.id.accountNav -> changeFragment(AccountFragment(userId!!))
                R.id.albumNav-> changeFragment(AlbumSingerFragment())
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
        val intent = Intent(this, MusicService::class.java)
        stopService(intent)
    }

    override fun onChanged(id: Long?) {
        binding.bottomNavigation.visibility = View.VISIBLE
        userId = id
    }
}