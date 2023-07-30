package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.FragmentRegisterSingerBinding
import com.example.basicmusicapp.repository.RepositoryUser
import com.example.basicmusicapp.viewmodels.ViewModelLoginRegisterFragment

class RegisterSingerFragment : Fragment() {
    private lateinit var binding: FragmentRegisterSingerBinding
    private lateinit var viewModelLoginRegisterFragment: ViewModelLoginRegisterFragment
    private var userName = ""
    private var password = ""
    private var email = ""
    private var singerName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterSingerBinding.inflate(inflater, container, false)
        viewModelLoginRegisterFragment =
            ViewModelProvider(this)[ViewModelLoginRegisterFragment::class.java]
        binding.btnSignUpSinger.setOnClickListener {
            signUp()
        }
        return binding.root
    }

    private fun signUp() {
        if (isEmpty()) {
            RegisterFragment.actionViewBegin()
            viewModelLoginRegisterFragment.registerViewModel(
                userName,
                password,
                email,
                singerName,
                object : RepositoryUser.OnLoginSigUpListener {
                    override fun onExits(exits: Boolean) {
                        if (exits) {
                            Toast.makeText(context, "UserName is exits", Toast.LENGTH_LONG)
                                .show()
                            RegisterFragment.actionViewEnd()
                        } else {
                            Toast.makeText(
                                context,
                                "Đăng ký thành công\n Quay lại đăng nhập để sử dụng dịch vụ",
                                Toast.LENGTH_LONG
                            ).show()
                            RegisterFragment.actionViewEnd()
                            emptyInfo()
                        }
                    }

                    override fun onLogin(confirm: Boolean, userId: Long) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    private fun isEmpty(): Boolean {
        userName = binding.editUserNameSinger.text.trim().toString()
        password = binding.editPasswordSinger.text.trim().toString()
        email = binding.editEmailSinger.text.trim().toString()
        singerName = binding.editNameSinger.text.toString()
        if (userName == "") {
            Toast.makeText(context, "Vui lòng điền tài khoản", Toast.LENGTH_LONG).show()
            return false
        }
        if (email == "") {
            Toast.makeText(context, "Vui lòng điền email", Toast.LENGTH_LONG).show()
            return false
        }
        if (password == "") {
            Toast.makeText(context, "Vui lòng điền mật khẩu", Toast.LENGTH_LONG).show()
            return false
        }
        if (singerName == "") {
            Toast.makeText(context, "Vui lòng điền tên ca sĩ", Toast.LENGTH_LONG).show()
            return false
        }
        if (binding.editPasswordSinger.text.trim()
                .toString() != binding.editConfirmPasswordSinger.text.trim()
                .toString()
        ) {
            Toast.makeText(context, "Xác nhận lại mật khẩu", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun emptyInfo() {
        binding.apply {
            editEmailSinger.text = null
            editPasswordSinger.text = null
            editUserNameSinger.text = null
            editConfirmPasswordSinger.text = null
            editNameSinger.text = null
        }
    }
}