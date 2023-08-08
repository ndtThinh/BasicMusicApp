package com.example.basicmusicapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.FragmentSettingAccountBinding
import com.example.basicmusicapp.viewmodels.ViewModelAccountFragment
import com.squareup.picasso.Picasso

class SettingAccountFragment(id: Long) : Fragment() {
    private var userIdCurrent: Long? = id
    private lateinit var binding: FragmentSettingAccountBinding
    private lateinit var viewModelAccountFragment: ViewModelAccountFragment
    private var imageUri: Uri? = null
    private var nameSinger: String = ""
    private var singerId: Long = 0
    private var imageUserUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingAccountBinding.inflate(inflater, container, false)
        viewModelAccountFragment = ViewModelProvider(this)[ViewModelAccountFragment::class.java]
        actionViewBegin()
        userIdCurrent?.let { setDataUser(it) }
        binding.apply {
            btnBackSetting.setOnClickListener {
                val fragmentManager = parentFragmentManager
                fragmentManager.popBackStack()
            }
            btnUpdateInfo.setOnClickListener {
                newUpDateUser()
            }
            btnChoseImage.setOnClickListener {
                choseImage()
            }
        }
        return binding.root
    }

    private fun newUpDateUser() {
        actionViewBegin()
        val userName = binding.tvSettingUserName.text.trim().toString()
        val password = binding.editSettingPassword.text.trim().toString()
        val email = binding.editSettingEmail.text.trim().toString()
        nameSinger = binding.editSettingNameSinger.text.toString()

        viewModelAccountFragment.newUpdateUser(userName, password, email,
            userIdCurrent!!, imageUri, imageUserUrl, nameSinger, singerId,
            object : ViewModelAccountFragment.OnUpDateUserListenerVm {
                override fun onUpdateViewModel(boolean: Boolean) {
                    if (boolean) {
                        Toast.makeText(
                            context,
                            "Cập nhập thông tin thành công",
                            Toast.LENGTH_LONG
                        ).show()
                        actionViewEnd()
                    } else {
                        Toast.makeText(
                            context,
                            "Cập nhập thông tin thất bại",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

    }

    @SuppressLint("SetTextI18n")
    private fun setDataUser(userId: Long) {
        viewModelAccountFragment.getListUser(userId)
            ?.observe(
                viewLifecycleOwner
            ) { t ->
                if (t != null) {
                    for (item in t) {
                        binding.apply {
                            if (item.singerName != "") {
                                tvKindOfAccount.text = "Nhà phát hành"
                                infoSettingSinger.visibility = View.VISIBLE
                                tvSingerIdSetting.text = item.singerId.toString()
                                editSettingNameSinger.setText(item.singerName)
                                singerId = item.singerId
                            } else {
                                tvKindOfAccount.text = "Người dùng cơ bản"
                            }
                            tvSettingUserName.text = item.userName
                            editSettingPassword.setText(item.passWord)
                            tvUserIdSetting.text = item.userId.toString()
                            editSettingEmail.setText(item.email)
                            imageUserUrl = item.fileImage
                            if (item.fileImage != "") {
                                Picasso.get().load(item.fileImage).into(binding.imgUserSetting)
                            } else {
                                binding.imgUserSetting.setImageResource(R.drawable.account_icon)
                            }
                            actionViewEnd()
                        }
                    }
                }
            }
    }

    private fun choseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null) {
            imageUri = data.data
            binding.imgUserSetting.setImageURI(imageUri)
        }
    }

    private fun actionViewBegin() {
        binding.progressBarAccountSettingFragment.visibility = View.VISIBLE
        binding.layoutSettingAccount.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarAccountSettingFragment.visibility = View.GONE
        binding.layoutSettingAccount.alpha = 1f
    }
}