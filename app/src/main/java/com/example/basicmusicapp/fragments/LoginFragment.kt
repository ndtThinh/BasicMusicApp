package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.FragmentLoginBinding
import com.example.basicmusicapp.repository.RepositoryUser
import com.example.basicmusicapp.viewmodels.ViewModelLoginRegisterFragment

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModelLoginRegisterFragment: ViewModelLoginRegisterFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var userName = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelLoginRegisterFragment =
            ViewModelProvider(this)[ViewModelLoginRegisterFragment::class.java]
        binding.apply {
            btnGoRegister.setOnClickListener {
                changeFragment(RegisterFragment())
            }
            btnLogin.setOnClickListener {
                logIn()
            }
        }
        return view
    }

    private fun logIn() {

        if (isEmptyInfo()) {
            actionViewBegin()
            viewModelLoginRegisterFragment.login(
                userName,
                password,
                object : RepositoryUser.OnLoginSigUpListener {
                    override fun onExits(exits: Boolean) {
                    }

                    override fun onLogin(confirm: Boolean) {
                        if (confirm) {
                            actionViewEnd()
                            changeFragment(ListSongFragment())
                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG)
                                .show()

                        } else {
                            actionViewEnd()
                            Toast.makeText(
                                context,
                                "Tài khoản hoặc mật khẩu không đúng",
                                Toast.LENGTH_LONG
                            ).show()

                        }
                    }

                })
        }
    }

    private fun isEmptyInfo(): Boolean {
        userName = binding.editUserNameLogin.text.trim().toString()
        password = binding.editPasswordLogin.text.trim().toString()
        if (userName == "") {
            Toast.makeText(context, "Vui lòng điền tài khoản", Toast.LENGTH_LONG).show()
            return false
        }
        if (password == "") {
            Toast.makeText(context, "Vui lòng điền mật khẩu", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack("ok")
        fragmentTransaction.commit()
    }

    private fun actionViewBegin() {
        binding.progressBarLogin.visibility = View.VISIBLE
        binding.layoutLogin.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarLogin.visibility = View.GONE
        binding.layoutLogin.alpha = 1f
    }
}