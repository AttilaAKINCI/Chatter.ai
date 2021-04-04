package com.akinci.chatter.feature.login.view

import android.animation.ValueAnimator.INFINITE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import androidx.transition.TransitionSet
import com.akinci.chatter.R
import com.akinci.chatter.common.component.SnackBar
import com.akinci.chatter.common.extension.validateMinCharacter
import com.akinci.chatter.common.helper.Resource
import com.akinci.chatter.databinding.FragmentLoginBinding
import com.akinci.chatter.feature.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = loginViewModel

        /** view transition configuration **/
        val enterTransitionSet = TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(context).inflateTransition(android.R.transition.move))
        enterTransitionSet.duration = 1000
        sharedElementEnterTransition = enterTransitionSet

        val enterFade = Fade()
        enterFade.startDelay = 1000
        enterFade.duration = 300
        enterTransition = enterFade

        val exitFade = Fade()
        exitFade.startDelay = 0
        exitFade.duration = 300
        exitTransition = exitFade
        /** **/

        /** Login Button action **/
        binding.btnLogin.setOnClickListener{
            // validate login fields.
            if(binding.etUserName.validateMinCharacter(2)){
                // fields are valid. Proceed to login.
                loginViewModel.actionLogin()
            }else{
                SnackBar.make(binding.root,  "Please check input fields.", SnackBar.LENGTH_LONG).show()
            }
        }

        Timber.d("LoginFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //login screen chatter icon animation
        binding.animation.apply {
            repeatCount = INFINITE
        }.playAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.loginEventHandler.observe(viewLifecycleOwner) {
            when(it){
                is Resource.Info -> {
                    // show info message on snackBar
                    SnackBar.make(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                }
                is Resource.Success -> {
                    it.data?.let { isUserVerified ->
                        if(isUserVerified){
                            /** Navigate user to chat dashboard. **/
                            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_chatDashboardFragment)
                        }
                    }
                }
                is Resource.Error -> {
                    // show info message on snackBar
                    SnackBar.makeLarge(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                }
            }
        }
    }

}