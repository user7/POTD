package com.geekbrains.potd.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.transition.Fade
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment() {
    private var _binding: FragmentAnimationBinding? = null
    private val binding: FragmentAnimationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnimationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonShow.setOnClickListener {
            TransitionManager.beginDelayedTransition(
                binding.animationContainer,
                AutoTransition()
            )
            if (binding.animationGroup.visibility == View.GONE) {
                binding.animationGroup.visibility = View.VISIBLE
                binding.buttonShow.setIconResource(R.drawable.ic_show_none)
            } else {
                binding.animationGroup.visibility = View.GONE
                binding.buttonShow.setIconResource(R.drawable.ic_show_all)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}