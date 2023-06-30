package com.example.basicmusicapp.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.Constants.ACTION_CLEAR
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.databinding.FragmentPlaySongBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs
import com.example.basicmusicapp.service.MyService
import java.text.SimpleDateFormat


class PlaySongFragment : Fragment() {
    private lateinit var binding: FragmentPlaySongBinding
    private var mediaPlayer: MediaPlayer? = null
    var index: Int = 0
    var song: Song? = null
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
        val filter = IntentFilter("action_music")
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(actionReceiver, filter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        endService()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(actionReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
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
                val fragment: ListSongFragment = ListSongFragment()
                val fragmentTransaction = fragmentManager.beginTransaction()
                val bundle = Bundle()
                bundle.putSerializable("song", song)
                fragment.arguments = bundle
                fragmentTransaction.replace(R.id.frameLayout,fragment)
                fragmentTransaction.commit()
//                fragmentManager.popBackStack()
            }
            btnNext.setOnClickListener {
                nextSong()
            }
            btnPrevious.setOnClickListener {
                previousSong()
            }
            btnPausePlay.setOnClickListener {
                pausePlaySong()
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
                    mediaPlayer!!.seekTo(binding.seekBar.progress)
                }

            })
            btnEndService.setOnClickListener {
                endService()
            }
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
                playSong(song!!.fileSong)
                setTimeTotal()
                beginService()
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
                index = DataSongs().listSongs.size
            } else {
                index--
            }
            setInitNextPrevSong()
        }
    }

    private fun setInitNextPrevSong() {
        stopPlayer()
        song = DataSongs().listSongs[index]
        playSong(song!!.fileSong)
        setTimeTotal()
        setUiSong()
        beginService()
    }

    private fun setUiSong() {
        binding.apply {
            TvNameSong.text = song!!.title
            TvNameSinger.text = song!!.singer
            imageViewSong.setImageResource(song!!.image)
        }
    }

    private fun pausePlaySong() {
        if (mediaPlayer!!.isPlaying) {
            binding.btnPausePlay.setImageResource(R.drawable.play_icon)
            mediaPlayer!!.pause()
            startMyReceiver(Constants.ACTION_PAUSE)
        } else {
            mediaPlayer!!.start()
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

    private fun playSong(audio: Int) {
        mediaPlayer = MediaPlayer.create(context, audio)
        mediaPlayer!!.start()
        mediaPlayer!!.setOnCompletionListener {
            stopPlayer()
            nextSong()
        }
        updateTimeSong()
    }

    private fun stopPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun setTimeTotal() {
        var simpleDateFormat = SimpleDateFormat("mm:ss")
        binding.apply {
            TvTimeTotals.text = simpleDateFormat.format(mediaPlayer!!.duration)
            seekBar.max = mediaPlayer!!.duration
        }
    }

    private fun updateTimeSong() {
        val handler: Handler = Handler()
        val updateRunnable = object : Runnable {
            override fun run() {
                var simpleDateFormat = SimpleDateFormat("mm:ss")
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    binding.apply {
                        TvTimeSong.text = simpleDateFormat.format(mediaPlayer!!.currentPosition)
                        seekBar.progress = mediaPlayer!!.currentPosition
                    }
                }
                if (mediaPlayer == null) {
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

    private fun beginService() {
        var intent = Intent(requireContext(), MyService::class.java)
        var bundle = Bundle()
        bundle.putSerializable("song_object", song!!)
        intent.putExtras(bundle)
        requireContext().startService(intent)
    }

    private fun endService() {
        val intent = Intent(requireContext(), MyService::class.java)
        requireContext().stopService(intent)
        stopPlayer()
        startMyReceiver(ACTION_CLEAR)
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constants.ACTION_PAUSE -> {
                // Xử lý action PAUSE
                Log.d("broadcast111", "pause")
                mediaPlayer!!.pause()
                binding.btnPausePlay.setImageResource(R.drawable.play_icon)
            }
            Constants.ACTION_RESUME -> {
                // Xử lý action RESUME
                Log.d("broadcast111", "play")
                mediaPlayer!!.start()
                binding.btnPausePlay.setImageResource(R.drawable.pause_icon)
            }
            Constants.ACTION_CLEAR -> {
                // Xử lý action CLEAR
            }
            Constants.ACTION_NEXT -> {
                nextSong()
            }
            Constants.ACTION_PREV -> {
                previousSong()
            }
        }
    }


}
