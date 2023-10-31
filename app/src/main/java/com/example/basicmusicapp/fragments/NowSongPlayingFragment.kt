package com.example.basicmusicapp.fragments

import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.PlayingSongActivity
import com.example.basicmusicapp.R
import com.example.basicmusicapp.broadcastreceiver.MyReceiver
import com.example.basicmusicapp.databinding.FragmentNowSongPlayingBinding
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.viewmodels.ViewModelNowSongPlaying
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
class NowSongPlayingFragment : Fragment() {

    var currentSong: SongMusic? = null
    var index: Int = 0
    var listSongCurrent = ArrayList<SongMusic>()
    private lateinit var viewModelNowSongPlayingFragment: ViewModelNowSongPlaying
    private lateinit var binding: FragmentNowSongPlayingBinding
    private val actionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.getIntExtra("action_music", 0) as Int
            handleActionMusic(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("fragmentNowSongPlaying", "on create")
    }

    override fun onStart() {
        super.onStart()
        Log.d("fragmentNowSongPlaying", "on start")
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
    ): View {
        binding = FragmentNowSongPlayingBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelNowSongPlayingFragment=ViewModelProvider(requireActivity())[ViewModelNowSongPlaying::class.java]
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
                changeToPlayingActivity()
            }
            linearLayout.setOnClickListener {
                changeToPlayingActivity()
            }
        }
        viewModelNowSongPlayingFragment.loadSong()
        viewModelNowSongPlayingFragment.liveDataSong.observe(viewLifecycleOwner){
            currentSong=it
            setUiSong()
        }
        return view
    }

    private fun changeToPlayingActivity() {
        val intent = Intent(activity, PlayingSongActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("song", currentSong)
        bundle.putInt("style", 0)
        bundle.putInt("index", index)
        bundle.putString("kind", Constants.SONG_PLAYING)
        intent.putExtra("listSong", listSongCurrent)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun initFragment() {
        val bundle = arguments
        if (bundle != null) {
            currentSong = bundle.getSerializable("songNow") as SongMusic
            index = bundle.getInt("index")
            listSongCurrent = bundle.getSerializable("listSong") as ArrayList<SongMusic>
            Log.d("ListSongCurrent:", "size:${listSongCurrent.size}")
            binding.apply {
                Picasso.get().load(currentSong!!.imageSong).into(imgSongPlaying)
                tvSingerNamePlaying.text = currentSong!!.nameSinger
                tvTitleSongPlaying.text = currentSong!!.nameSong
            }
        }
    }

    private fun handleActionMusic(action: Int) {
        when (action) {
            Constants.ACTION_PAUSE -> {
                // Xử lý action PAUSE
                Log.d("Action Music", "pause")
                binding.btnPausePlayNotificationPlaying.setImageResource(R.drawable.play_icon)
            }
            Constants.ACTION_RESUME -> {
                // Xử lý action RESUME
                Log.d("Action Music", "play")
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
                Log.d("FragmentPlaying", "Next SOng")
            }
            Constants.ACTION_PREV -> {
                //Xử lý action PREVIOUS
                previousSong()
                Log.d("FragmentPlaying", "Prev SOng")
            }
        }
    }

    private fun pausePlaySong() {
        if (PlayingSongActivity.songService!!.isPlaying()) {
            startMyReceiver(Constants.ACTION_PAUSE)
        } else {
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
        index++
        if (index == listSongCurrent.size) {
            index = 0
        }
        currentSong = listSongCurrent[index]
        setUiSong()
    }

    private fun previousSong() {
        index--
        if (index == -1) {
            index = listSongCurrent.size - 1
        }
        currentSong = listSongCurrent[index]
        setUiSong()
    }


    private fun setUiSong() {
        binding.apply {
            tvTitleSongPlaying.text = currentSong!!.nameSong
            tvSingerNamePlaying.text = currentSong!!.nameSinger
            Picasso.get().load(currentSong!!.imageSong).into(imgSongPlaying)
            btnPausePlayNotificationPlaying.setImageResource(R.drawable.pause_icon)
        }
    }


}