package com.example.basicmusicapp.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basicmusicapp.databinding.LayoutItemSongBinding
import com.example.basicmusicapp.models.Song

class SongAdapter(
    private val listSong: ArrayList<Song>,
    private val playListener: onClickListener
) : RecyclerView.Adapter<SongAdapter.MyViewHolder>() {
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
        val song = listSong[position]
        holder.apply {
            title.text = song.title
            singer.text = song.singer
            image.setImageResource(song.image)
        }
        holder.root.setOnClickListener {
            playListener.onClickToPlaySong(song, position)
        }
        holder.moreAction.setOnClickListener {
            playListener.onClickToMoreAction(song, position)
        }
    }

    override fun getItemCount(): Int {
        return listSong.size
    }

    interface onClickListener {
        fun onClickToPlaySong(song: Song, index: Int)
        fun onClickToMoreAction(song: Song, index: Int)
    }
}