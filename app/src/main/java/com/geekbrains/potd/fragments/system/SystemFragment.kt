package com.geekbrains.potd.fragments.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentSystemBinding

class SystemFragment : Fragment() {
    private var _binding: FragmentSystemBinding? = null
    val binding: FragmentSystemBinding get() = _binding!!

    val fragmentViewModel: SystemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewModel.state.observe(viewLifecycleOwner) { renderState(it) }
        binding.chipNextDay.setOnClickListener { fragmentViewModel.adjustDayShift(1) }
        binding.chipPrevDay.setOnClickListener { fragmentViewModel.adjustDayShift(-1) }
        binding.chipResetDate.setOnClickListener { fragmentViewModel.resetDayShift() }
        binding.chipHD.setOnClickListener { fragmentViewModel.adjustDayShift(0) }
        fragmentViewModel.sendServerRequest()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: SystemViewModel.RequestState) {
        when (state) {
            is SystemViewModel.RequestState.Error -> {
                binding.imageView.setImageResource(R.drawable.ic_load_error_vector)
                binding.description.setText(state.error)
            }
            is SystemViewModel.RequestState.Loading -> {
                binding.imageView.setImageResource(R.drawable.bg_system)
                binding.description.setText(R.string.label_description_loading)
            }
            is SystemViewModel.RequestState.Success -> {
                val url = if (binding.chipHD.isChecked) state.response.hdurl else state.response.url
                binding.imageView.load(url) {}
                binding.description.setText(state.response.title)
            }
        }
    }
}