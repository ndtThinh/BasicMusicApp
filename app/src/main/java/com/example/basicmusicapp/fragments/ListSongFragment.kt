package com.example.basicmusicapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SongAdapter
import com.example.basicmusicapp.databinding.FragmentListSongBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs
import com.example.basicmusicapp.service.MyService

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class ListSongFragment : Fragment() {
    private lateinit var binding: FragmentListSongBinding
    private lateinit var songAdapter: SongAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListSongBinding.inflate(inflater, container, false)
        val view = binding.root
        init()
        getSmallSongPlaying()
        return view
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
        val bundle = arguments
        if(bundle!=null){
            val view= requireActivity().findViewById<RelativeLayout>(R.id.frameLayout_song_playing)
            view.visibility=View.VISIBLE
        }
    }
}