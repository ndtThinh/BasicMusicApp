package com.example.basicmusicapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.basicmusicapp.R
import com.example.basicmusicapp.models.SlideItem
import com.makeramen.roundedimageview.RoundedImageView

class SlideAdapter : RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {
    private var slideItems: ArrayList<SlideItem>? = null
    private var viewPager2: ViewPager2? = null

    constructor(slideItems: ArrayList<SlideItem>, viewPager2: ViewPager2) {
        this.slideItems = slideItems
        this.viewPager2 = viewPager2
    }

    class SlideViewHolder : RecyclerView.ViewHolder {
        private lateinit var imageView: RoundedImageView

        constructor(itemView: View) : super(itemView) {
            this.imageView = itemView.findViewById(R.id.imageSlide)
        }

        fun setImage(slideItem: SlideItem) {
            imageView.setImageResource(slideItem.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        return SlideViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.slide_item_container, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.setImage(slideItems!![position])
        if(position==slideItems!!.size-2){
            viewPager2!!.post(sliderRunnable)
        }
    }

    override fun getItemCount(): Int {
        return  slideItems!!.size
    }

    private var sliderRunnable=object :Runnable{
        override fun run() {
            slideItems!!.addAll(slideItems!!)
            notifyDataSetChanged()
        }

    }
}