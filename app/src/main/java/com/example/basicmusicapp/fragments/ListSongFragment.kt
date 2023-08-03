package com.example.basicmusicapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SongAdapter
import com.example.basicmusicapp.databinding.FragmentListSongBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.repository.DataSongs
import java.text.Normalizer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
class ListSongFragment : Fragment() {
    private lateinit var binding: FragmentListSongBinding
    private lateinit var songAdapter: SongAdapter
    var isBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
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
    }

    private fun init() {
        binding.musicRV.setHasFixedSize(true)
        binding.musicRV.layoutManager = LinearLayoutManager(context)
        songAdapter = SongAdapter(DataSongs().listSongs, object : SongAdapter.OnClickListener {
            override fun onClickToPlaySong(song: Song, index: Int) {
                changeFragmentPlaySong(song, index)
            }

            override fun onClickToMoreAction(song: Song, index: Int) {
                showDialog(song)
            }
        })
        binding.btnPlayRandom.setOnClickListener {
            setRandomSong()
        }
        binding.musicRV.adapter = songAdapter
        binding.tvTotalSongs.text = "Tổng số bài hát: ${DataSongs().listSongs.size}"
        binding.searchBoxListSong.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var mListSong = ArrayList<Song>()
                for (item in DataSongs().listSongs) {
                    var removeDiaLowerStr =
                        removeVietnameseDiacritics(item.singer).toLowerCase(Locale.ROOT)
                    if (removeDiaLowerStr.contains(newText!!))
                        mListSong.add(item)
                }
                songAdapter.setFilter(mListSong = mListSong)
                return true
            }

        })
    }

    fun removeVietnameseDiacritics(input: String): String {
        val temp = Normalizer.normalize(input, Normalizer.Form.NFD)
        return temp.replace("\\p{M}".toRegex(), "")
    }

    private fun showDialog(song: Song) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.bottomsheetlayout)
        val layoutDownload = dialog?.findViewById<LinearLayout>(R.id.layoutDownload)
        val layoutAddToFavourite = dialog?.findViewById<LinearLayout>(R.id.layoutAddFavourite)
        val layoutAddToPlayList = dialog?.findViewById<LinearLayout>(R.id.layoutAddToPlayList)
        val layoutGoToAlbum = dialog?.findViewById<LinearLayout>(R.id.layoutGoToAlbum)
        val layoutGoToArtist = dialog?.findViewById<LinearLayout>(R.id.layoutGoToArtist)
        val imageDialog = dialog?.findViewById<ImageView>(R.id.ivSongDialog)
        val tvTittleSongDialog = dialog?.findViewById<TextView>(R.id.TvTitleSongItemDialog)
        val tvSingerDialog = dialog?.findViewById<TextView>(R.id.TvSingerItemDialog)
        imageDialog?.setImageResource(song.image)
        tvTittleSongDialog?.text = song.title
        tvSingerDialog?.text = song.singer
        layoutDownload?.setOnClickListener {
            Toast.makeText(context, "clicked download", Toast.LENGTH_LONG).show()
        }
        layoutAddToFavourite?.setOnClickListener {
            Toast.makeText(context, "clicked favorite", Toast.LENGTH_LONG).show()
        }
        layoutAddToPlayList?.setOnClickListener {
            Toast.makeText(context, "clicked add to playlist", Toast.LENGTH_LONG).show()
        }
        layoutGoToAlbum?.setOnClickListener {
            Toast.makeText(context, "clicked go to album", Toast.LENGTH_LONG).show()
        }
        layoutGoToArtist?.setOnClickListener {
            Toast.makeText(context, "clicked go to artist", Toast.LENGTH_LONG).show()
        }
        dialog?.show()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)
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

    private fun setRandomSong() {
        val randomIndex = Random.nextInt(0, DataSongs().listSongs.size)
        changeFragmentPlaySong(DataSongs().listSongs[randomIndex], randomIndex)
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
        } else {
            Log.d("small:", "music null")
        }
    }
}