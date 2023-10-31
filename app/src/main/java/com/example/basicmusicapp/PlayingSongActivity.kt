package com.example.basicmusicapp

import android.annotation.SuppressLint
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.databinding.ActivityPlayingSongBinding
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.service.SongService
import com.example.basicmusicapp.viewmodels.ViewModelNowSongPlaying
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat

class PlayingSongActivity : AppCompatActivity(), ServiceConnection {
    private lateinit var binding: ActivityPlayingSongBinding
    private lateinit var viewModelNowSongPlaying: ViewModelNowSongPlaying
    private var listSong = ArrayList<SongMusic>()
    private var currentSong: SongMusic? = null
    private var kind: String = ""
    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.getIntExtra("action_music", 0)
            handleActionMusic(action)
        }
    }

    companion object {
        var isBound: Boolean = false
        var songService: SongService? = null
        var index: Int = 0
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constants.ACTION_PAUSE -> {
                // Xử lý action PAUSE
                Log.d("Action Music", "pause")
                binding.btnPausePlayActivity.setImageResource(R.drawable.play_icon)
            }
            Constants.ACTION_RESUME -> {
                // Xử lý action RESUME
                Log.d("Action Music", "play")
                binding.btnPausePlayActivity.setImageResource(R.drawable.pause_icon)
            }
            Constants.ACTION_CLEAR -> {
                // Xử lý action CLEAR
                binding.apply {
                    TvTimeSongPlay.text = "00:00"
                    TvTimeTotalsPlay.text = "00:00"
                    seekBarPlay.progress = 0
                }
                disconnectFromService()
            }
            Constants.ACTION_NEXT -> {
                //Xử lý action NEXT
                nextSong()
                setInfoSong(currentSong!!)
                Log.d("SongCurrent", "Next SOng")
            }
            Constants.ACTION_PREV -> {
                //Xử lý action PREVIOUS
                prevSong()
                setInfoSong(currentSong!!)
                Log.d("SongCurrent", "Prev SOng")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connectToService()
        val filter = IntentFilter("action_music")
        LocalBroadcastManager.getInstance(this).registerReceiver(actionReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromService()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(actionReceiver)
        Log.d("PlayingSong", "On destroy activity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayingSongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModelNowSongPlaying =
            ViewModelProvider(this)[ViewModelNowSongPlaying::class.java]
        getDataFromActivity()
        binding.apply {
            seekBarPlay.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    songService!!.seekTo(binding.seekBarPlay.progress)
                }

            })
            btnPausePlayActivity.setOnClickListener {
                pauseAndPlay()
            }
            btnBackPlayingSong.setOnClickListener {
                finish()
            }
            btnAddToFavourite.setOnClickListener {
                btnAddToFavourite.setImageResource(R.drawable.favorite_icon)
            }
        }
    }

    private fun pauseAndPlay() {
        if (songService!!.isPlaying()) {
            binding.btnPausePlayActivity.setImageResource(R.drawable.play_icon)
            startMyReceiver(Constants.ACTION_PAUSE)
        } else {
            binding.btnPausePlayActivity.setImageResource(R.drawable.pause_icon)
            startMyReceiver(Constants.ACTION_RESUME)
        }
    }

    private fun startMyReceiver(action: Int) {
        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("action_music", action)
        this.sendBroadcast(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun getDataFromActivity() {
        currentSong = intent.getSerializableExtra("song") as SongMusic
        listSong = intent.getStringArrayListExtra("listSong") as ArrayList<SongMusic>
        val style = intent.getIntExtra("style", 0)
        index = intent.getIntExtra("index", 0)
        kind = intent.getStringExtra("kind").toString()
        when (style) {
            Constants.STYLE_LOVE -> binding.tvStyleSong.text = "Nhạc lãng mạn"
            Constants.STYLE_SAD -> binding.tvStyleSong.text = "Nhạc buồn"
            Constants.STYLE_RAP -> binding.tvStyleSong.text = "Nhạc Rap"
            Constants.STYLE_REMIX -> binding.tvStyleSong.text = "Nhạc Remix"
            Constants.STYLE_RED -> binding.tvStyleSong.text = "Nhạc cách mạng"
            Constants.STYLE_CHINA -> binding.tvStyleSong.text = "Nhạc Hoa"
            Constants.STYLE_EURO -> binding.tvStyleSong.text = "Nhạc Âu Mỹ"
            Constants.STYLE_BEAT -> binding.tvStyleSong.text = "Nhạc Không Lời"
            Constants.STYLE_FUN -> binding.tvStyleSong.text = "Nhạc tươi vui"
            Constants.STYLE_LOFI -> binding.tvStyleSong.text = "Nhạc Lofi Chill"
            0 -> binding.tvStyleSong.text = "World of Music"
        }
    }

    private fun setInfoSong(songMusic: SongMusic) {
        binding.apply {
            TvNameSongPlay.text = songMusic.nameSong
            TvNameSingerPlay.text = songMusic.nameSinger
            Picasso.get().load(songMusic.imageSong).into(imageViewSongPlayingActivity)
        }
        setTimeTotal()
        updateTimeSong()
    }

    private fun setTimeTotal() {
        val simpleDateFormat = SimpleDateFormat("mm:ss")
        if (isBound) {
            binding.apply {
                if (kind == Constants.NEW_SONG_PLAY) {
                    TvTimeTotalsPlay.text = simpleDateFormat.format(songService!!.duration())
                    seekBarPlay.max = songService!!.duration()
                    Log.d(
                        "SongCurrent",
                        "Duration: ${songService!!.mediaPlayer!!.duration}+${songService!!.mediaPlayer!!.isPlaying}"
                    )
                    songService!!.sendNotification(songService!!.currentSong!!)
                } else {
                    TvTimeTotalsPlay.text = simpleDateFormat.format(songService!!.duration())
                    seekBarPlay.max = songService!!.duration()
                    Log.d(
                        "SongCurrent",
                        "Duration: ${songService!!.duration()}+${songService!!.isPlaying()}"
                    )
                    songService!!.sendNotification(songService!!.currentSong!!)
                }

            }
        } else {
            Toast.makeText(this, "No service", Toast.LENGTH_LONG).show()
            Log.d("Service connect", "No service")
        }
    }

    private fun updateTimeSong() {
        val handler: Handler = Handler()
        val updateRunnable = object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                var simpleDateFormat = SimpleDateFormat("mm:ss")
                if (songService != null && songService!!.isPlaying()) {
                    binding.apply {
                        TvTimeSongPlay.text =
                            simpleDateFormat.format(songService!!.currentPosition())
                        seekBarPlay.progress = songService!!.currentPosition()
                    }
                }
                if (songService == null) {
                    binding.apply {
                        TvTimeSongPlay.text = "00:00"
                        TvTimeTotalsPlay.text = "00:00"
                        seekBarPlay.progress = 0
                    }
                }
                handler.postDelayed(this, 500)
            }
        }
        handler.postDelayed(updateRunnable, 100)
    }

    private fun nextSong() {
        index++
        if (index == listSong.size) {
            index = 0
        }
        currentSong = listSong[index]
        binding.btnPausePlayActivity.setImageResource(R.drawable.pause_icon)
    }

    private fun prevSong() {
        index--
        if (index < 0) {
            index = listSong.size - 1
        }
        currentSong = listSong[index]
    }

    private fun connectToService() {
        val intent = Intent(this, SongService::class.java)
        bindService(intent, this, Context.BIND_AUTO_CREATE)
        startService(intent)
    }

    private fun disconnectFromService() {
        unbindService(this)
        Log.d("Action", "unbind service")
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as (SongService.MySongBinder)
        songService = binder.getService()
        isBound = true
        Log.d("SongService", "SongService is connected")
        if (kind == Constants.NEW_SONG_PLAY) {
            songService!!.setSongCurrent(currentSong!!, index, listSong)
        }
        viewModelNowSongPlaying.loadMediaPlayer()
        viewModelNowSongPlaying.liveDataMedia.observe(this) {
            if (it != null) {
                setInfoSong(songService!!.currentSong!!)
            }
        }
        binding.apply {
            btnPausePlayActivity.setOnClickListener {
                pauseAndPlay()
            }
            btnNextPlay.setOnClickListener {
                startMyReceiver(Constants.ACTION_NEXT)
                Log.d("Action Music", "next")
            }
            btnPreviousPlay.setOnClickListener {
                startMyReceiver(Constants.ACTION_PREV)
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Log.d("SongService", "SongService is disconnected")
    }
}