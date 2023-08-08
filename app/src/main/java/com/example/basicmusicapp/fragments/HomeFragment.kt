package com.example.basicmusicapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.MainActivity
import com.example.basicmusicapp.PlayingSongActivity
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SlideAdapter
import com.example.basicmusicapp.adapters.SongMusicAdapter
import com.example.basicmusicapp.databinding.FragmentHomeBinding
import com.example.basicmusicapp.models.SlideItem
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.viewmodels.ViewModelHomeFragment
import java.text.Normalizer
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModelHomeFragment: ViewModelHomeFragment

    //adapter
    private lateinit var romanceSongAdapter: SongMusicAdapter
    private lateinit var sadSongAdapter: SongMusicAdapter
    private lateinit var chinaSongAdapter: SongMusicAdapter
    private lateinit var chillSongAdapter: SongMusicAdapter
    private lateinit var beatSongAdapter: SongMusicAdapter
    private lateinit var remixSongAdapter: SongMusicAdapter
    private lateinit var funSongAdapter: SongMusicAdapter
    private lateinit var euroSongAdapter: SongMusicAdapter
    private lateinit var redMusicSongAdapter: SongMusicAdapter
    private lateinit var rapSongAdapter: SongMusicAdapter

    var handler = Handler()
    private var slideItems = ArrayList<SlideItem>()
    private var mListSong = ArrayList<SongMusic>()
    private lateinit var searchAdapter: SongMusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModelHomeFragment = ViewModelProvider(this)[ViewModelHomeFragment::class.java]
        viewModelHomeFragment.loadSong()
        slideViewFun()
        initFragment()
        binding.searchBoxNameSong.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                var newListSong=ArrayList<SongMusic>()
//                binding.rcvSongSearch.visibility=View.VISIBLE
//                binding.rcvSongSearch.setHasFixedSize(true)
//                binding.rcvSongSearch.layoutManager=LinearLayoutManager(context)
//                searchAdapter= SongMusicAdapter(newListSong,object :SongMusicAdapter.OnclickListener{
//                    override fun onClickToPlaySong(song: SongMusic, index: Int) {
//
//                    }
//
//                    override fun onClickToMoreAction(song: SongMusic, index: Int) {
//
//                    }
//
//                })
//                binding.rcvSongSearch.adapter=searchAdapter
//                for (item in mListSong) {
//                    var removeDiaLowerStr =
//                        removeVietnameseDiacritics(item.nameSong).toLowerCase(Locale.ROOT)
//                    if (removeDiaLowerStr.contains(newText!!))
//                        newListSong.add(item)
//                }
//                searchAdapter.setFilter(newListSong)
                return true
            }
        })

        return binding.root
    }

    fun removeVietnameseDiacritics(input: String): String {
        val temp = Normalizer.normalize(input, Normalizer.Form.NFD)
        return temp.replace("\\p{M}".toRegex(), "")
    }

    private fun initFragment() {
        actionViewBegin()
        viewModelHomeFragment.liveDataSongLove.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //love song
                mListSong = t
                binding.rcvRomanceSong.setHasFixedSize(true)
                binding.rcvRomanceSong.layoutManager = LinearLayoutManager(context)
                romanceSongAdapter = SongMusicAdapter(t,
                    object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_LOVE)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvRomanceSong.adapter = romanceSongAdapter
                actionViewEnd()
            }
        }
        viewModelHomeFragment.liveDataSongSad.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //sad song
                binding.rcvSadSong.setHasFixedSize(true)
                binding.rcvSadSong.layoutManager = LinearLayoutManager(context)
                sadSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_SAD)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvSadSong.adapter = sadSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongChina.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //china song
                binding.rcvChinaSong.setHasFixedSize(true)
                binding.rcvChinaSong.layoutManager = LinearLayoutManager(context)
                chinaSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_CHINA)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvChinaSong.adapter = chinaSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongChill.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //chill song
                binding.rcvChillSong.setHasFixedSize(true)
                binding.rcvChillSong.layoutManager = LinearLayoutManager(context)
                chillSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_LOFI)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvChillSong.adapter = chillSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongBeat.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //beat song
                binding.rcvBeatSong.setHasFixedSize(true)
                binding.rcvBeatSong.layoutManager = LinearLayoutManager(context)
                beatSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_BEAT)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvBeatSong.adapter = beatSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongRemix.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //remix song
                binding.rcvRemixSong.setHasFixedSize(true)
                binding.rcvRemixSong.layoutManager = LinearLayoutManager(context)
                remixSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_REMIX)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvRemixSong.adapter = remixSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongFun.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //fun song
                binding.rcvFunSong.setHasFixedSize(true)
                binding.rcvFunSong.layoutManager = LinearLayoutManager(context)
                funSongAdapter = SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                    override fun onClickToPlaySong(song: SongMusic, index: Int) {
                        Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                        sendData(song, index, t, Constants.STYLE_FUN)
                    }

                    override fun onClickToMoreAction(song: SongMusic, index: Int) {
                        Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                    }
                })
                binding.rcvFunSong.adapter = funSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongEuro.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //Euro song
                binding.rcvEuroSong.setHasFixedSize(true)
                binding.rcvEuroSong.layoutManager = LinearLayoutManager(context)
                euroSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_EURO)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvEuroSong.adapter = euroSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongRedMusic.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //Euro song
                binding.rcvRedMusicSong.setHasFixedSize(true)
                binding.rcvRedMusicSong.layoutManager = LinearLayoutManager(context)
                redMusicSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_RED)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvRedMusicSong.adapter = redMusicSongAdapter
            }
        }
        viewModelHomeFragment.liveDataSongRap.observe(viewLifecycleOwner) { t ->
            if (t!!.isNotEmpty()) {
                //Euro song
                binding.rcvRapSong.setHasFixedSize(true)
                binding.rcvRapSong.layoutManager = LinearLayoutManager(context)
                rapSongAdapter =
                    SongMusicAdapter(t, object : SongMusicAdapter.OnclickListener {
                        override fun onClickToPlaySong(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to song", Toast.LENGTH_LONG).show()
                            sendData(song, index, t, Constants.STYLE_RAP)
                        }

                        override fun onClickToMoreAction(song: SongMusic, index: Int) {
                            Toast.makeText(context, "click to action", Toast.LENGTH_LONG).show()
                        }
                    })
                binding.rcvRapSong.adapter = rapSongAdapter
            }
        }

    }

    private fun sendData(
        songMusic: SongMusic,
        index: Int,
        listSongMusic: ArrayList<SongMusic>,
        style: Int
    ) {
        val intent = Intent(activity, PlayingSongActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("song", songMusic)
        bundle.putInt("style", style)
        intent.putExtra("listSong", listSongMusic)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun slideViewFun() {
        slideItems.add(SlideItem(R.drawable.imagehome1))
        slideItems.add(SlideItem(R.drawable.imagehome2))
        slideItems.add(SlideItem(R.drawable.imagehome3))
        slideItems.add(SlideItem(R.drawable.imagehome4))
        slideItems.add(SlideItem(R.drawable.imagehome5))
        binding.viewPager2.adapter = SlideAdapter(slideItems, binding.viewPager2)
        binding.apply {
            viewPager2.clipToPadding = false
            viewPager2.clipChildren = false
            viewPager2.offscreenPageLimit = 4
            viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        val compositionTransform = CompositePageTransformer()
        compositionTransform.addTransformer(MarginPageTransformer(30))
        compositionTransform.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPager2.setPageTransformer(compositionTransform)
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(sliderRunnable)
                handler.postDelayed(sliderRunnable, 3000)
            }
        })
    }

    private var sliderRunnable =
        Runnable { binding.viewPager2.currentItem = binding.viewPager2.currentItem + 1 }

    private fun actionViewBegin() {
        binding.progressBarHomeFragment.visibility = View.VISIBLE
        binding.layoutHomeFragmentContent.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarHomeFragment.visibility = View.GONE
        binding.layoutHomeFragmentContent.alpha = 1f
    }
}