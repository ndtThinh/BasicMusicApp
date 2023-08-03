package com.example.basicmusicapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.basicmusicapp.R
import com.example.basicmusicapp.Splash
import com.example.basicmusicapp.databinding.FragmentAccountBinding
import com.example.basicmusicapp.repository.RepositoryImage
import com.example.basicmusicapp.repository.RepositoryUser
import com.example.basicmusicapp.viewmodels.ViewModelAccountFragment
import com.squareup.picasso.Picasso


class AccountFragment : Fragment {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModelAccountFragment: ViewModelAccountFragment
    private var userIdCurrent: Long? = null
    private var singerIdCurrent: Long = 0
    private var singerName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    constructor(userId: Long) {
        userIdCurrent = userId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModelAccountFragment =
            ViewModelProvider(this)[ViewModelAccountFragment::class.java]
        actionViewBegin()
        userIdCurrent?.let { setInfoUser(it) }
        binding.apply {
            downloadSongsBtn.setOnClickListener {
                changeFragment(ListSongFragment())
            }
            btnLogOut.setOnClickListener {
                context?.let { it1 -> RepositoryUser().logOutUser(it1) }
                Toast.makeText(context, "Logout user ", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    requireContext().applicationContext,
                    Splash::class.java
                )
                startActivity(intent)
            }
            btnSettingAccount.setOnClickListener {
                userIdCurrent?.let { it1 -> SettingAccountFragment(it1) }
                    ?.let { it2 -> changeFragment(it2) }
            }
            btnMySongs.setOnClickListener {
                if (singerIdCurrent == 0L) {
                    Toast.makeText(
                        context,
                        "Tài khoản của bạn không có quyền truy cập này",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    changeFragment(UpLoadSongFragment(singerIdCurrent, singerName))
                }
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setInfoUser(userId: Long) {
        viewModelAccountFragment.getListUser(userId)
            ?.observe(
                viewLifecycleOwner
            ) { t ->
                if (t != null) {
                    for (item in t) {
                        if (item.fileImage == "") {
                            binding.imgUser.setImageResource(R.drawable.account_icon)
                        } else {
                            Picasso.get().load(item.fileImage).into(binding.imgUser)
                        }
                        binding.apply {
                            tvUserName.text = item.userName
                            tvEmail.text = item.email
                            singerIdCurrent = item.singerId
                            singerName = item.singerName
                        }
                        actionViewEnd()
                    }
                }
            }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack("ok")
        fragmentTransaction.commit()
    }

    private fun actionViewBegin() {
        binding.progressBarAccountFragment.visibility = View.VISIBLE
        binding.layoutAccountFragment.alpha = 0.5f
    }

    private fun actionViewEnd() {
        binding.progressBarAccountFragment.visibility = View.GONE
        binding.layoutAccountFragment.alpha = 1f
    }
}