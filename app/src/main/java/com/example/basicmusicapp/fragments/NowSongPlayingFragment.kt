package com.example.basicmusicapp.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.databinding.FragmentNowSongPlayingBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs

// TODO: Rename parameter arguments, choose names that match
class NowSongPlayingFragment : Fragment() {

    var song: Song? = null
    var index: Int = 0
    private lateinit var binding: FragmentNowSongPlayingBinding
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

    override fun onResume() {
        super.onResume()
        initFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(actionReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNowSongPlayingBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.apply {
            btnPausePlayNotificationPlaying.setOnClickListener {
                pausePlaySong()
            }
            btnNextNotificationPlaying.setOnClickListener {
                startMyReceiver(Constants.ACTION_NEXT)
                btnPausePlayNotificationPlaying.setImageResource(R.drawable.pause_icon)
            }
            btnPrevNotificationPlaying.setOnClickListener {
                startMyReceiver(Constants.ACTION_PREV)
                btnPausePlayNotificationPlaying.setImageResource(R.drawable.pause_icon)
            }
            btnClearNotificationPlaying.setOnClickListener {
                val view =
                    requireActivity().findViewById<FrameLayout>(R.id.frameLayout_song_playing)
                view.visibility = View.GONE
                startMyReceiver(Constants.ACTION_CLEAR)
            }
            imgSongPlaying.setOnClickListener {
                changeToPlaySongFragment()
            }
            tvTitleSongPlaying.setOnClickListener {
                changeToPlaySongFragment()
            }
        }
        return view
    }

    private fun changeToPlaySongFragment() {
        val fragmentPlaySongFragment: PlaySongFragment = PlaySongFragment()
        var fragmentManager = parentFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putSerializable("song", song)
        bundle.putInt("index", index)
        fragmentPlaySongFragment.arguments = bundle
        fragmentTransaction.replace(R.id.frameLayout, fragmentPlaySongFragment)
        fragmentTransaction.addToBackStack("ok")
        fragmentTransaction.commit()
        val view =
            requireActivity().findViewById<FrameLayout>(R.id.frameLayout_song_playing)
        view.visibility = View.GONE
    }

    private fun initFragment() {
        if (PlaySongFragment.musicService!!.isPlaying()) {
//            val currentSong= PlaySongFragment.musicService!!.currentSong
            val bundle = arguments
            if (bundle != null) {
                song = bundle.getSerializable("songNow") as Song
                index = bundle.getInt("index")
                binding.apply {
//                    if (currentSong != null) {
//                        imgSongPlaying.setImageResource(currentSong.image)
//                        tvSingerNamePlaying.text = currentSong!!.singer
//                        tvTitleSongPlaying.text = currentSong!!.title
//                    }
                    imgSongPlaying.setImageResource(song!!.image)
                    tvSingerNamePlaying.text = song!!.singer
                    tvTitleSongPlaying.text = song!!.title

                }
            }
        }
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constants.ACTION_PAUSE -> {
                // Xử lý action PAUSE
                Log.d("Action Music", "pause")
//                musicService!!.pause()
                Log.d("Action Music", PlaySongFragment.musicService!!.isPlaying().toString())
                binding.btnPausePlayNotificationPlaying.setImageResource(R.drawable.play_icon)
            }
            Constants.ACTION_RESUME -> {
                // Xử lý action RESUME
                Log.d("Action Music", "play")
//                musicService!!.play()
                Log.d("Action Music", PlaySongFragment.musicService!!.isPlaying().toString())
                binding.btnPausePlayNotificationPlaying.setImageResource(R.drawable.pause_icon)
            }
            Constants.ACTION_CLEAR -> {
                // Xử lý action CLEAR
                val view =
                    requireActivity().findViewById<FrameLayout>(R.id.frameLayout_song_playing)
                view.visibility = View.GONE
            }
            Constants.ACTION_NEXT -> {
                //Xử lý action NEXT
                nextSong()
                Log.d("FragmentPlay", "Next SOng")
            }
            Constants.ACTION_PREV -> {
                //Xử lý action PREVIOUS
                previousSong()
                Log.d("FragmentPlay", "Prev SOng")
            }
        }
    }

    private fun pausePlaySong() {
        if (PlaySongFragment.musicService!!.isPlaying()) {
            binding.btnPausePlayNotificationPlaying.setImageResource(R.drawable.play_icon)
            startMyReceiver(Constants.ACTION_PAUSE)
        } else {
            binding.btnPausePlayNotificationPlaying.setImageResource(R.drawable.pause_icon)
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
//        song= PlaySongFragment.musicService?.currentSong
        setUiSong()
    }

    private fun setUiSong() {
        binding.apply {
            tvTitleSongPlaying.text = song!!.title
            tvSingerNamePlaying.text = song!!.singer
            imgSongPlaying.setImageResource(song!!.image)
            btnPausePlayNotificationPlaying.setImageResource(R.drawable.pause_icon)
        }
    }

}