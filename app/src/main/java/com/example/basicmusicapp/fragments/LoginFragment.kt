package com.example.basicmusicapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.FragmentLoginBinding
import com.example.basicmusicapp.repository.RepositoryUser
import com.example.basicmusicapp.viewmodels.ViewModelLoginRegisterFragment

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModelLoginRegisterFragment: ViewModelLoginRegisterFragment
    private lateinit var onChangScreen: OnChangScreen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    interface OnChangScreen {
        fun onChanged(id: Long?)
    }

    private var userName = ""
    private var password = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onChangScreen = context as (OnChangScreen)
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnButtonClickListener")
        }
    }
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

                    override fun onLogin(confirm: Boolean, userId: Long) {
                        if (confirm) {
                            actionViewEnd()
                            Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG)
                                .show()
                            context?.let {
                                viewModelLoginRegisterFragment.keepLoginUser(
                                    userId,
                                    it
                                )
                            }
                            changeFragment(HomeFragment())
                            onChangScreen.onChanged(userId)
                        } else {
                            actionViewEnd()
                            Toast.makeText(
                                context,
                                "Tài khoản hoặc mật khẩu không đúng",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.tvMessageLogin.visibility = View.VISIBLE
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