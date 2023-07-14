package com.example.basicmusicapp.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SongAdapter
import com.example.basicmusicapp.databinding.FragmentListSongBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs
import com.example.basicmusicapp.service.MusicService
import com.example.basicmusicapp.service.MyService

// TODO: Rename parameter arguments, choose names that match
class ListSongFragment : Fragment() {
    private lateinit var binding: FragmentListSongBinding
    private lateinit var songAdapter: SongAdapter
    var isBound: Boolean = false
    private var musicService: MusicService? = null
//    private val serviceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//            val binder = service as (MusicService.MyBinder)
//            musicService = binder.getService()
//            isBound = true
//            if(musicService!=null){
//                Log.d("ListSong", isBound.toString()+ musicService!!.currentSong?.title + musicService!!.isPlaying() )
//            }
//            Log.d("ListSong", isBound.toString()+ musicService!!.currentSong?.title + musicService!!.isPlaying() )
//        }
//
//        override fun onServiceDisconnected(name: ComponentName?) {
//            isBound=false
//            Log.d("ListSong", isBound.toString()+ musicService!!.currentSong?.title)
//        }
//
//    }

//    private fun connectToService() {
//        val intent = Intent(requireContext(), MusicService::class.java)
//        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
//    }
//
//    private fun disconnectFromService() {
//        if (isBound) {
//            requireContext().unbindService(serviceConnection)
//            isBound = false
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
//        connectToService()
        Log.d("ListSong", "on start")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListSongBinding.inflate(inflater, container, false)
        val view = binding.root
        init()
        return view
    }

    override fun onResume() {
        super.onResume()
        getSmallSongPlaying()
        Log.d("ListSong", "on resume")
    }

    override fun onDestroy() {
        super.onDestroy()
//        disconnectFromService()
    }

    private fun init() {
        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.layoutManager = LinearLayoutManager(context)
        songAdapter = SongAdapter(DataSongs().listSongs, object : SongAdapter.onClickListener {
            override fun onClick(song: Song, index: Int) {
                changeFragmentPlaySong(song, index)
            }
        })
        binding.musicRV.adapter = songAdapter
        binding.tvTotalSongs.text = "Tổng số bài hát: ${DataSongs().listSongs.size}"
    }

    private fun changeFragmentPlaySong(song: Song, index: Int) {
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
    }

    private fun getSmallSongPlaying() {
        if (PlaySongFragment.musicService != null) {
            val view = requireActivity().findViewById<FrameLayout>(R.id.frameLayout_song_playing)
            view.visibility = View.VISIBLE
            val fragmentNowSongPlaying = NowSongPlayingFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.frameLayout_song_playing, fragmentNowSongPlaying)
            val bundle = Bundle()
            bundle.putSerializable("songNow", PlaySongFragment.musicService!!.currentSong)
            bundle.putInt("index", PlaySongFragment.index)
            fragmentNowSongPlaying.arguments = bundle
            fragmentTransaction?.commit()
            Log.d("FragmentListSong", "get small")
        }
    }
}