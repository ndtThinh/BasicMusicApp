package com.example.basicmusicapp.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.basicmusicapp.databinding.LayoutItemSongBinding
import com.example.basicmusicapp.models.SongMusic
import com.squareup.picasso.Picasso

class SongMusicAdapter(
    var listSong: ArrayList<SongMusic>,
    var onclickListener: OnclickListener
) : RecyclerView.Adapter<SongMusicAdapter.MyViewHolder>() {
    interface OnclickListener {
        fun onClickToPlaySong(song: SongMusic, index: Int)
        fun onClickToMoreAction(song: SongMusic, index: Int)
    }

    class MyViewHolder(binding: LayoutItemSongBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.TvTitleSongItem
        val singer = binding.TvSingerItem
        val image = binding.ivSong
        val moreAction = binding.moreActionBtn
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val songMusic = listSong[position]
        holder.apply {
            title.text = songMusic.nameSong
            singer.text = songMusic.nameSinger
            Picasso.get().load(songMusic.imageSong).into(image)
        }
        holder.root.setOnClickListener {
            onclickListener.onClickToPlaySong(songMusic, position)
        }
        holder.moreAction.setOnClickListener {
            onclickListener.onClickToMoreAction(songMusic, position)
        }
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    fun setFilter(mListSong: ArrayList<SongMusic>) {
        this.listSong = mListSong
        notifyDataSetChanged()
    }
}

