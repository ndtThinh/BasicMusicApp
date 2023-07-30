package com.example.basicmusicapp.fragments

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
import com.example.basicmusicapp.models.User
import com.example.basicmusicapp.repository.RepositoryImage
import com.example.basicmusicapp.viewmodels.ViewModelAccountFragment

class SettingAccountFragment : Fragment {
    private var userIdCurrent: Long? = null
    private lateinit var binding: FragmentSettingAccountBinding
    private lateinit var viewModelAccountFragment: ViewModelAccountFragment
    private var imageUri: Uri? = null
    private var isHasImage: Boolean = false
    private var nameSinger: String = ""
    private var singerId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    constructor(id: Long) {
        userIdCurrent = id
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingAccountBinding.inflate(inflater, container, false)
        viewModelAccountFragment = ViewModelProvider(this)[ViewModelAccountFragment::class.java]
        actionViewBegin()
        userIdCurrent?.let { setDataUser(it) }
        binding.apply {
            btnBackSetting.setOnClickListener {
                var fragmentManager = parentFragmentManager
                fragmentManager.popBackStack()
            }
            btnUpdateInfo.setOnClickListener {
                upDateUser()
            }
            btnChoseImage.setOnClickListener {
                choseImage()
            }
        }
        return binding.root
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

    private fun upDateUser() {
        actionViewBegin()
        var userName = binding.tvSettingUserName.text.trim().toString()
        var password = binding.editSettingPassword.text.trim().toString()
        var email = binding.editSettingEmail.text.trim().toString()
        nameSinger = binding.editSettingNameSinger.text.toString()
        var updateUser: User? = null
        if (isHasImage) {
            var fileImage = userName
            updateUser =
                userIdCurrent?.let {
                    User(userName, password, email, it, fileImage, nameSinger, singerId)
                }
        } else {
            if (imageUri != null) {
                var fileImage = userName
                updateUser =
                    userIdCurrent?.let {
                        User(userName, password, email, it, fileImage, nameSinger, singerId)
                    }
            } else {
                updateUser = userIdCurrent?.let {
                    User(userName, password, email, it, "", nameSinger, singerId)
                }
            }
        }
        if (updateUser != null) {
            viewModelAccountFragment.updateUser(
                updateUser,
                imageUri,
                isHasImage,
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
    }

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
                            val mRepositoryImage = RepositoryImage()
                            mRepositoryImage.readImage(item.userName,
                                object : RepositoryImage.OnGetImageListener {
                                    override fun onGetImage(boolean: Boolean) {
                                        tvSettingUserName.text = item.userName
                                        editSettingPassword.setText(item.passWord)
                                        tvUserIdSetting.text = item.userId.toString()
                                        editSettingEmail.setText(item.email)
                                        if (boolean) {
                                            binding.imgUserSetting.setImageBitmap(mRepositoryImage.bitmap)
                                            isHasImage = true
                                        } else {
                                            binding.imgUserSetting.setImageResource(R.drawable.account_icon)
                                            isHasImage = false
                                        }
                                        actionViewEnd()
                                    }

                                })
                        }
                    }
                }
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