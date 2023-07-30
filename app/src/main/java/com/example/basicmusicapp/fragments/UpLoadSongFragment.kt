package com.example.basicmusicapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.Constants
import com.example.basicmusicapp.databinding.FragmentUpLoadSongBinding
import com.example.basicmusicapp.viewmodels.ViewModelUploadSongFragment


class UpLoadSongFragment : Fragment {
    private lateinit var binding: FragmentUpLoadSongBinding
    private lateinit var viewModelUploadSongFragment: ViewModelUploadSongFragment
    private var singerId: Long = 0
    private var nameSinger: String = ""
    private var nameSong: String = ""

    //    private var listStyles:List<Integer>
    private var listStyles = mutableListOf<Int>()
    private var imageUri: Uri? = null
    private var songUri: Uri? = null

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.imvCheckDoneNameSongs.visibility = View.VISIBLE
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    constructor(singerId: Long, singerName: String) {
        this.singerId = singerId
        this.nameSinger = singerName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpLoadSongBinding.inflate(inflater, container, false)
        binding.tvNameSingerUpLoad.text = nameSinger
        viewModelUploadSongFragment =
            ViewModelProvider(this)[ViewModelUploadSongFragment::class.java]
        binding.apply {
            btnUploadSong.setOnClickListener {
                uploadSong()
            }
            btnSelectUriImage.setOnClickListener {
                selectImage()
            }
            btnSelectUriAudio.setOnClickListener {
                selectAudio()
            }
            tvNameSongUpLoad.addTextChangedListener(textWatcher)
        }
        return binding.root
    }

    private fun selectAudio() {
        val intent = Intent()
        intent.type = "audio/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 150)
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null) {
            imageUri = data.data
            binding.imvSongUpload.setImageURI(imageUri)
        }
        if (requestCode == 150 && data != null && data.data != null) {
            songUri = data.data
            binding.tvUriFileSong.text = songUri.toString()
        }
    }

    private fun uploadSong() {
        getValues()
        viewModelUploadSongFragment.upLoadSongViewModel(
            nameSong,
            nameSinger,
            listStyles,
            singerId,
            imageUri!!,
            songUri!!,
            object : ViewModelUploadSongFragment.OnUploadSongViewModelLis {
                override fun onUpload(boolean: Boolean) {
                    if (boolean) {
                        Toast.makeText(
                            context,
                            "Upload new song successfully: $nameSong",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Upload new song failed: $nameSong",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            })
    }

    private fun getValues() {
        nameSong = binding.tvNameSongUpLoad.text.toString()
        binding.apply {
            val arrayList = ArrayList<Int>()
            if (ckbBeat.isChecked) {
                listStyles.add(Constants.STYLE_BEAT)
            }
            if (ckbChill.isChecked) {
                listStyles.add(Constants.STYLE_LOFI)
            }
            if (ckbRap.isChecked) {
                listStyles.add(Constants.STYLE_RAP)
            }
            if (ckbFunny.isChecked) {
                listStyles.add(Constants.STYLE_FUN)
            }
            if (ckbSad.isChecked) {
                listStyles.add(Constants.STYLE_SAD)
            }
            if (ckbChinaMusic.isChecked) {
                listStyles.add(Constants.STYLE_CHINA)
            }
            if (ckbEuroMusic.isChecked) {
                listStyles.add(Constants.STYLE_EURO)
            }
            if (ckbRedMusic.isChecked) {
                listStyles.add(Constants.STYLE_RED)
            }
            if (ckbRemix.isChecked) {
                listStyles.add(Constants.STYLE_REMIX)
            }
            if (ckbRomance.isChecked) {
                listStyles.add(Constants.STYLE_LOVE)
            }

        }
    }


}