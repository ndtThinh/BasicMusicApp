package com.example.basicmusicapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.R
import com.example.basicmusicapp.databinding.FragmentRegisterBinding
import com.example.basicmusicapp.repository.RepositoryUser
import com.example.basicmusicapp.viewmodels.ViewModelLoginRegisterFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class RegisterFragment : Fragment() {
    private lateinit var viewModelLoginRegisterFragment: ViewModelLoginRegisterFragment

    companion object {
        private lateinit var binding: FragmentRegisterBinding
        fun actionViewBegin() {
            binding.progressBarSignUp.visibility = View.VISIBLE
            binding.layoutSignUp.alpha = 0.5f
        }

        fun actionViewEnd() {
            binding.progressBarSignUp.visibility = View.GONE
            binding.layoutSignUp.alpha = 1f
        }
    }


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
        changeFragment(RegisterUserFragment())
        binding.apply {
            menuRegister.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.itemUserSimple -> changeFragment(RegisterUserFragment())
                    R.id.itemSinger -> changeFragment(RegisterSingerFragment())
                }
                return@setOnItemSelectedListener true
            }
            btnBack.setOnClickListener {
                val fragmentManager = parentFragmentManager
                fragmentManager.popBackStack()
            }
        }
        return view
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layoutRegisters, fragment)
        fragmentTransaction.commit()
    }

}