package com.example.basicmusicapp.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.basicmusicapp.R
import com.example.basicmusicapp.adapters.SlideAdapter
import com.example.basicmusicapp.databinding.FragmentHomeBinding
import com.example.basicmusicapp.models.SlideItem
import kotlin.math.abs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    var handler = Handler()
    var slideItems = ArrayList<SlideItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        slideViewFun()

        return binding.root
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
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(sliderRunnable)
                handler.postDelayed(sliderRunnable,3000)
            }
        })
    }

    private var sliderRunnable= Runnable { binding.viewPager2.currentItem = binding.viewPager2.currentItem+1 }

}