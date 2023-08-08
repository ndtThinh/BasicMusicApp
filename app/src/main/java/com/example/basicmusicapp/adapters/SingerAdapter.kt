package com.example.basicmusicapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.ItemSingerBinding
import com.example.basicmusicapp.databinding.LayoutItemSongBinding
import com.example.basicmusicapp.models.Song
import com.example.basicmusicapp.models.SongMusic
import com.example.basicmusicapp.models.User
import com.squareup.picasso.Picasso

class SingerAdapter(var listSinger: ArrayList<User>, var onclickListener: OnclickListener) :
    RecyclerView.Adapter<SingerAdapter.MyViewHolder>() {
    interface OnclickListener {
        fun onClickListSong(singerId: Long, singerName: String)
    }

    class MyViewHolder(binding: ItemSingerBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageSinger = binding.imageViewSinger
        val textViewSinger = binding.textViewNameSinger
        val item = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemSingerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = listSinger[position]
        if(user.fileImage!=""){
            Picasso.get().load(user.fileImage).into(holder.imageSinger)
        }else{
            holder.imageSinger.setImageResource(R.drawable.music_player )
        }
        holder.textViewSinger.text = user.singerName
        holder.item.setOnClickListener {
            onclickListener.onClickListSong(user.singerId, user.singerName)
        }
    }

    override fun getItemCount(): Int {
        return listSinger.size
    }
    fun setFilter(mListSinger: ArrayList<User>) {
        this.listSinger = mListSinger
        notifyDataSetChanged()
    }
}