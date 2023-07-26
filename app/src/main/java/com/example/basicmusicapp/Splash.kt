package com.example.basicmusicapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.basicmusicapp.Constants.DELAY_TIME
import com.example.basicmusicapp.databinding.ActivitySplashBinding
import com.example.basicmusicapp.repository.RepositoryUser

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            val id: Long = RepositoryUser().checkLogged(this)
            val intent = Intent(this@Splash, MainActivity::class.java)
            if (id > 0) {
                intent.putExtra("UserId", id)
            }
            startActivity(intent)
            finish()
        }, DELAY_TIME.toLong())
    }
}