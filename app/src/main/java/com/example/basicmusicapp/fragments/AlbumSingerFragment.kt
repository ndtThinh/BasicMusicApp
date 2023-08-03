package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SingerAdapter
import com.example.basicmusicapp.databinding.FragmentAlbumSingerBinding
import com.example.basicmusicapp.viewmodels.ViewModelAlbumSongSingerFragment

class AlbumSingerFragment : Fragment() {

    private lateinit var binding: FragmentAlbumSingerBinding
    private lateinit var viewModelAlbumSongSingerFragment: ViewModelAlbumSongSingerFragment
    private lateinit var mSingerAdapter: SingerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumSingerBinding.inflate(inflater, container, false)
        viewModelAlbumSongSingerFragment =
            ViewModelProvider(this)[ViewModelAlbumSongSingerFragment::class.java]

        initFragment()


        return binding.root
    }

    private fun initFragment() {
        viewModelAlbumSongSingerFragment.getAllUser()
        viewModelAlbumSongSingerFragment.liveDataUserSinger.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mSingerAdapter = SingerAdapter(it, object : SingerAdapter.OnclickListener {
                    override fun onClickListSong(singerId: Long, singerName: String) {
                        Toast.makeText(context, "Click the item $singerName", Toast.LENGTH_LONG)
                            .show()
                    }
                })
                val gridLayout = GridLayoutManager(context, 3)
                binding.rcvSongOfSinger.layoutManager = gridLayout
                binding.rcvSongOfSinger.adapter = mSingerAdapter
            }
        }
    }

}