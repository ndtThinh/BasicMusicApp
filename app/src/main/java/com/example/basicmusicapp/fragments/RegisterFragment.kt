package com.example.basicmusicapp.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.databinding.FragmentRegisterBinding
import com.example.basicmusicapp.repository.RepositoryUser
import com.example.basicmusicapp.viewmodels.ViewModelLoginRegisterFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModelLoginRegisterFragment: ViewModelLoginRegisterFragment


    private var userName = ""
    private var password = ""
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelLoginRegisterFragment =
            ViewModelProvider(this).get(ViewModelLoginRegisterFragment::class.java)
        binding.apply {
            btnBack.setOnClickListener {
                val fragmentManager = parentFragmentManager
                fragmentManager.popBackStack()
            }
            btnSignUp.setOnClickListener {
                signIn()
            }
        }
        return view
    }

    private fun signIn() {
        if (isEmpty()) {
            actionViewBegin()
            viewModelLoginRegisterFragment.registerViewModel(
                userName,
                password,
                email,
                object : RepositoryUser.OnLoginSigUpListener {
                    override fun onExits(exits: Boolean) {
                        if (exits) {
                            Toast.makeText(context, "UserName is exits", Toast.LENGTH_LONG)
                                .show()
                            actionViewEnd()
                        } else {
                            Toast.makeText(
                                context,
                                "Đăng ký thành công\n Quay lại đăng nhập để sử dụng dịch vụ",
                                Toast.LENGTH_LONG
                            ).show()
                            actionViewEnd()
                            emptyInfo()
                        }
                    }

                    override fun onLogin(confirm: Boolean) {
                    }
                })
        }
    }

    private fun isEmpty(): Boolean {
        userName = binding.editUserName.text.trim().toString()
        password = binding.editPassword.text.trim().toString()
        email = binding.editEmail.text.trim().toString()
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
        if (binding.editPassword.text.trim().toString() != binding.editConfirmPassword.text.trim()
                .toString()
        ) {
            Toast.makeText(context, "Xác nhận lại mật khẩu", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun emptyInfo() {
        binding.apply {
            editEmail.text = null
            editPassword.text = null
            editUserName.text = null
            editConfirmPassword.text = null
        }
    }

    private fun actionViewBegin() {
        binding.progressBarSignUp.visibility = View.VISIBLE
        binding.layoutSignUp.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarSignUp.visibility = View.GONE
        binding.layoutSignUp.alpha = 1f
    }
}