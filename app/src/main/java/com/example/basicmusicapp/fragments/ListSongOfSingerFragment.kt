package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SongMusicAdapter
import com.example.basicmusicapp.databinding.FragmentListSongOfSingerlBinding
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.viewmodels.ViewModelListSongFragment
import com.squareup.picasso.Picasso


class ListSongOfSingerFragment(singerId: Long, nameSinger: String) : Fragment() {

    private var currentSingerId = singerId
    private var currentNameSinger = nameSinger
    private lateinit var binding: FragmentListSongOfSingerlBinding
    private lateinit var viewModelListSongFragment: ViewModelListSongFragment
    private lateinit var songMusicAdapter: SongMusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListSongOfSingerlBinding.inflate(inflater, container, false)
        viewModelListSongFragment = ViewModelProvider(this)[ViewModelListSongFragment::class.java]
        initFragment()
        return binding.root
    }

    private fun initFragment() {
        actionViewBegin()
        binding.apply {
            tvNameSinger.text = "Ca sĩ: $currentNameSinger"
        }
        viewModelListSongFragment.getDataSongBySingerId(currentSingerId)
        viewModelListSongFragment.getDataSinger(currentSingerId)
        viewModelListSongFragment.listSongLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.tvTotalSongOfSinger.text = "Tổng số bài hát: ${it.size}"
                binding.rcvSongOfSinger.setHasFixedSize(true)
                binding.rcvSongOfSinger.layoutManager = LinearLayoutManager(context)
                songMusicAdapter = SongMusicAdapter(it, object : SongMusicAdapter.OnclickListener {
                    override fun onClickToPlaySong(song: SongMusic, index: Int) {

                    }

                    override fun onClickToMoreAction(song: SongMusic, index: Int) {
                    }
                })
                binding.rcvSongOfSinger.adapter = songMusicAdapter
                actionViewEnd()
            }
        }
        viewModelListSongFragment.singerCurrentLiveData.observe(viewLifecycleOwner){
            if(it!=null){
                Picasso.get().load(it.fileImage).into(binding.imgImageSinger)
                actionViewEnd()
            }
        }
    }

    private fun actionViewBegin() {
        binding.progressBarListSongOfSingerFragment.visibility = View.VISIBLE
        binding.layoutListSongOfSingerFragment.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarListSongOfSingerFragment.visibility = View.GONE
        binding.layoutListSongOfSingerFragment.alpha = 1f
    }
}