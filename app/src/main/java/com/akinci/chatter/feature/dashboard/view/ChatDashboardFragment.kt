package com.akinci.chatter.feature.dashboard.view

import android.content.Intent
import android.graphics.Shader
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.akinci.chatter.MainActivity
import com.akinci.chatter.R
import com.akinci.chatter.common.component.SnackBar
import com.akinci.chatter.common.component.TileDrawable
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.databinding.FragmentChatDashboardBinding
import com.akinci.chatter.databinding.FragmentLoginBinding
import com.akinci.chatter.feature.dashboard.adapter.ChatListAdapter
import com.akinci.chatter.feature.dashboard.viewmodel.ChatDashboardViewModel
import com.akinci.chatter.feature.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChatDashboardFragment : Fragment() {

    lateinit var binding: FragmentChatDashboardBinding
    private val chatDashboardViewModel : ChatDashboardViewModel by viewModels()

    lateinit var chatListAdapter : ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        /** Initialization of ViewBinding not need for DataBinding here **/
        binding = FragmentChatDashboardBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        // show appbar on chat dashboard
        (activity as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

        // set tile background
        val backgroundDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.pattern)
        binding.tileBackground.setImageDrawable(TileDrawable(backgroundDrawable!!, Shader.TileMode.REPEAT))

        Timber.d("ChatDashboardFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // fetch initial chat history.
        chatDashboardViewModel.fetchInitials()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_log_out) {
            /** initiate log out action here **/
            chatDashboardViewModel.logout()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatDashboardViewModel.loggedInUser.observe(viewLifecycleOwner){
            // fetch username and update appbar title
            (activity as AppCompatActivity).supportActionBar?.title = it.nickname
            chatListAdapter = ChatListAdapter(it.id)
        }

        chatDashboardViewModel.recentMessages.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Success -> {
                    // submit fetched data to Messaging List
                    binding.recyclerList.adapter = chatListAdapter
                    chatListAdapter.submitList(it.data)
                }
            }
        }

        chatDashboardViewModel.eventHandler.observe(viewLifecycleOwner){
            when(it){
                is Resource.Info -> {
                    // show info message on snackBar
                    SnackBar.make(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                }
                is Resource.Success -> {
                    /** ====Sign out action ====
                     * relaunch main activity.
                     * POST DELAYED ACTION ON VIEW MODEL (2000 ms)
                     * **/
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }

}