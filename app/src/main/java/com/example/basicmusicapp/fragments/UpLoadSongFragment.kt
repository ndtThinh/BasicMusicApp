package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.FragmentUpLoadSongBinding


class UpLoadSongFragment : Fragment() {
    private lateinit var binding: FragmentUpLoadSongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpLoadSongBinding.inflate(inflater, container, false)

        return binding.root
    }

}