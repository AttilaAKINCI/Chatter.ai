package com.akinci.chatter.feature.splash.view

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import com.akinci.chatter.databinding.FragmentSplashBinding
import com.akinci.chatter.R
import timber.log.Timber

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        /** Initialization of ViewBinding not need for DataBinding here **/
        binding = FragmentSplashBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) { navigateToLogin() }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })

        Timber.d("SplashFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.animation.playAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hide appbar on splash screen
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun navigateToLogin(){
        Handler(Looper.getMainLooper()).postDelayed({
            val imageTransition = resources.getString(R.string.image_transition)

            val extras = FragmentNavigatorExtras(
                binding.animation to imageTransition
            )

            /** Navigate to Login Page **/
            NavHostFragment.findNavController(this).navigate(
                R.id.action_splashFragment_to_loginFragment,
                null,
                null,
                extras
            )
        }, 100)
    }

}