package com.akinci.chatter.feature.dashboard.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.akinci.chatter.R
import com.akinci.chatter.databinding.FragmentChatDashboardBinding
import com.akinci.chatter.databinding.FragmentLoginBinding
import com.akinci.chatter.feature.dashboard.viewmodel.ChatDashboardViewModel
import com.akinci.chatter.feature.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChatDashboardFragment : Fragment() {

    lateinit var binding: FragmentChatDashboardBinding
    private val chatDashboardViewModel : ChatDashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_dashboard, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
      //  binding.vm = chatDashboardViewModel


        Timber.d("ChatDashboardFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


}