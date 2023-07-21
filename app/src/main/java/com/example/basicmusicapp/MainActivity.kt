package com.example.basicmusicapp

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import com.example.basicmusicapp.databinding.ActivityMainBinding
import com.example.basicmusicapp.fragments.AccountFragment
import com.example.basicmusicapp.fragments.ListSongFragment
import com.example.basicmusicapp.service.MusicService
import com.example.basicmusicapp.service.MyService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        addFragment(ListSongFragment())
        binding.apply {
            accountBtn.setOnClickListener {
                changeFragment(AccountFragment())
            }
            allSongBtn.setOnClickListener {
                changeFragment(ListSongFragment())
            }
        }

    }

    private fun addFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
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
}