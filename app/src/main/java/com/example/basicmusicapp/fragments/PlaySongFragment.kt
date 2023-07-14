package com.example.basicmusicapp.fragments

import android.content.*
import android.media.MediaPlayer
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.Constants.ACTION_CLEAR
import com.example.basicmusicapp.Constants.ACTION_PAUSE
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.databinding.FragmentPlaySongBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs
import com.example.basicmusicapp.service.MusicService
import com.example.basicmusicapp.service.MyService
import java.text.SimpleDateFormat


class PlaySongFragment : Fragment(), ServiceConnection {
    private lateinit var binding: FragmentPlaySongBinding
    var song: Song? = null


    companion object {
        var isBound: Boolean = false
        var musicService: MusicService? = null
        var index: Int = 0
    }

    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.getIntExtra("action_music", 0) as Int
            handleActionMusic(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        connectToService()
        val filter = IntentFilter("action_music")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(actionReceiver, filter)
    }

    override fun onStop() {
        super.onStop()
        Log.d("PlaySong", "on stop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(actionReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
//        disconnectFromService()
        Log.d("PlaySong", "on destroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaySongBinding.inflate(inflater, container, false)
        val view = binding.root
        getSongFromHome()

        binding.apply {
            backBtn.setOnClickListener {
                val fragmentManager = parentFragmentManager
                fragmentManager.popBackStack()
            }
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    musicService!!.seekTo(binding.seekBar.progress)
                }

            })
        }

        return view
    }

    private fun getSongFromHome() {
        val bundle = arguments
        if (bundle != null) {
            song = bundle.getSerializable("song") as Song
            index = bundle.getInt("index")
            binding.apply {
                setUiSong()
            }
        }
    }

    private fun nextSong() {
        if (index != null) {
            if (index == DataSongs().listSongs.size - 1) {
                index = 0
            } else {
                index++
            }
            setInitNextPrevSong()
        }

    }

    private fun previousSong() {
        if (index != null) {
            if (index == 0) {
                index = DataSongs().listSongs.size - 1
            } else {
                index--
            }
            setInitNextPrevSong()
        }
    }

    private fun setInitNextPrevSong() {
        song = DataSongs().listSongs[index]
//        song = musicService!!.currentSong
        setTimeTotal()
        setUiSong()
        updateTimeSong()
    }

    private fun setUiSong() {
        binding.apply {
            TvNameSong.text = song!!.title
            TvNameSinger.text = song!!.singer
            imageViewSong.setImageResource(song!!.image)
            layoutFragmentPlaySong.setBackgroundResource(song!!.image)
        }
    }

    private fun pausePlaySong() {
        if (musicService!!.isPlaying()) {
            binding.btnPausePlay.setImageResource(R.drawable.play_icon)
            startMyReceiver(Constants.ACTION_PAUSE)
        } else {
            binding.btnPausePlay.setImageResource(R.drawable.pause_icon)
            startMyReceiver(Constants.ACTION_RESUME)
        }
    }

    private fun startMyReceiver(action: Int) {
        val intent = Intent(
            requireContext(), MyReceiver
            ::class.java
        )
        intent.putExtra("action_music", action)
        requireContext().sendBroadcast(intent)
    }

    private fun setTimeTotal() {
        var simpleDateFormat = SimpleDateFormat("mm:ss")
        if (isBound) {
            binding.apply {
                TvTimeTotals.text = simpleDateFormat.format(musicService!!.duration())
                seekBar.max = musicService!!.duration()
            }
        } else {
            Toast.makeText(context, "No service", Toast.LENGTH_LONG).show()
            Log.d("Service connect", "khong co service")
        }

    }

    private fun updateTimeSong() {
        val handler: Handler = Handler()
        val updateRunnable = object : Runnable {
            override fun run() {
                var simpleDateFormat = SimpleDateFormat("mm:ss")
                if (musicService != null && musicService!!.isPlaying()) {
                    binding.apply {
                        TvTimeSong.text = simpleDateFormat.format(musicService!!.currentPosition())
                        seekBar.progress = musicService!!.currentPosition()
                    }
                }
                if (musicService == null) {
                    binding.apply {
                        TvTimeSong.text = "00:00"
                        TvTimeTotals.text = "00:00"
                        seekBar.progress = 0
                    }
                }
                handler.postDelayed(this, 500)
            }
        }
        handler.postDelayed(updateRunnable, 100)
    }

    private fun connectToService() {
        val intent = Intent(requireContext(), MusicService::class.java)
        requireContext().bindService(intent, this, Context.BIND_AUTO_CREATE)
//        requireContext().startService(intent)
    }

    private fun disconnectFromService() {
        if (isBound) {
            requireContext().unbindService(this)
            isBound = false
            musicService = null
            Log.d("Action", "unbind service")
        }
    }


    private fun handleActionMusic(action: Int) {
        when (action) {
            Constants.ACTION_PAUSE -> {
                // Xử lý action PAUSE
                Log.d("Action Music", "pause")
//                musicService!!.pause()
                Log.d("Action Music", musicService!!.isPlaying().toString())
                binding.btnPausePlay.setImageResource(R.drawable.play_icon)
            }
            Constants.ACTION_RESUME -> {
                // Xử lý action RESUME
                Log.d("Action Music", "play")
//                musicService!!.play()
                Log.d("Action Music", musicService!!.isPlaying().toString())
                binding.btnPausePlay.setImageResource(R.drawable.pause_icon)
            }
            Constants.ACTION_CLEAR -> {
                // Xử lý action CLEAR
                binding.apply {
                    TvTimeSong.text = "00:00"
                    TvTimeTotals.text = "00:00"
                    seekBar.progress = 0
                }
                disconnectFromService()
            }
            Constants.ACTION_NEXT -> {
                //Xử lý action NEXT
                nextSong()
//                setInitNextPrevSong()
                Log.d("FragmentPlay", "Next SOng")
            }
            Constants.ACTION_PREV -> {
                //Xử lý action PREVIOUS
                previousSong()
//                setInitNextPrevSong()
                Log.d("FragmentPlay", "Prev SOng")
            }
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as (MusicService.MyBinder)
        musicService = binder.getService()
        isBound = true
        Log.d("service connected", isBound.toString())
        if (song != null) {
            musicService!!.setSong(song!!, index)
            setTimeTotal()
            updateTimeSong()
        } else {
            Toast.makeText(context, "Khong cos bai hat nao ", Toast.LENGTH_LONG).show()
        }
        binding.apply {
            btnPausePlay.setOnClickListener {
                pausePlaySong()
            }
            btnNext.setOnClickListener {
                startMyReceiver(Constants.ACTION_NEXT)
                Log.d("Action Music", "next")
            }
            btnPrevious.setOnClickListener {
                startMyReceiver(Constants.ACTION_PREV)
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        isBound = false
        musicService = null
    }


}
