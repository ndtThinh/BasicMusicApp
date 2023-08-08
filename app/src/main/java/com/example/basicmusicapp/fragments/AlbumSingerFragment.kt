package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SingerAdapter
import com.example.basicmusicapp.databinding.FragmentAlbumSingerBinding
import com.example.basicmusicapp.models.User
import com.example.basicmusicapp.repository.DataSongs
import com.example.basicmusicapp.viewmodels.ViewModelAlbumSongSingerFragment
import java.text.Normalizer
import java.util.*
import kotlin.collections.ArrayList

class AlbumSingerFragment : Fragment() {

    private lateinit var binding: FragmentAlbumSingerBinding
    private lateinit var viewModelAlbumSongSingerFragment: ViewModelAlbumSongSingerFragment
    private lateinit var mSingerAdapter: SingerAdapter
    private var mListSinger=ArrayList<User>()
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
        searchBox()

        return binding.root
    }

    private fun searchBox(){
        binding.apply {
            searchBoxNameSinger.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    var newListSinger=ArrayList<User>()
                    for (item in mListSinger) {
                        var removeDiaLowerStr =
                            removeVietnameseDiacritics(item.singerName).toLowerCase(Locale.ROOT)
                        if (removeDiaLowerStr.contains(newText!!))
                            newListSinger.add(item)
                    }
                    mSingerAdapter.setFilter(newListSinger)
                    return true
                }
            })
        }
    }
    private fun initFragment() {
        actionViewBegin()
        viewModelAlbumSongSingerFragment.getAllUser()
        viewModelAlbumSongSingerFragment.liveDataUserSinger.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mListSinger=it
                mSingerAdapter = SingerAdapter(it, object : SingerAdapter.OnclickListener {
                    override fun onClickListSong(singerId: Long, singerName: String) {
                        changeFragment(ListSongOfSingerFragment(singerId,singerName))
                        Toast.makeText(context, "Click the item $singerName", Toast.LENGTH_LONG)
                            .show()
                    }
                })
                val gridLayout = GridLayoutManager(context, 3)
                binding.rcvSongOfSinger.layoutManager = gridLayout
                binding.rcvSongOfSinger.adapter = mSingerAdapter
                actionViewEnd()
            }
        }
    }

    fun removeVietnameseDiacritics(input: String): String {
        val temp = Normalizer.normalize(input, Normalizer.Form.NFD)
        return temp.replace("\\p{M}".toRegex(), "")
    }
    private fun changeFragment(fragment: Fragment) {
        val fragmentTransition = parentFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frameLayout, fragment)
        fragmentTransition.addToBackStack("oke")
        fragmentTransition.commit()
    }
    private fun actionViewBegin() {
        binding.progressBarAlbumFragment.visibility = View.VISIBLE
        binding.layoutAlbumFragment.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarAlbumFragment.visibility = View.GONE
        binding.layoutAlbumFragment.alpha = 1f
    }
}