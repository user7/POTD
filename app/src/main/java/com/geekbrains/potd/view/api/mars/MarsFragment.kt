package com.geekbrains.potd.view.api.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentMarsBinding
import com.geekbrains.potd.view.api.mars.MarsViewModel

class MarsFragment : Fragment() {
    private var _binding: FragmentMarsBinding? = null
    val binding: FragmentMarsBinding get() = _binding!!

    val marsViewModel: MarsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        marsViewModel.state.observe(viewLifecycleOwner) { renderState(it) }
        binding.chipNextDay.setOnClickListener { marsViewModel.adjustDate(1) }
        binding.chipPrevDay.setOnClickListener { marsViewModel.adjustDate(-1) }
        binding.chipResetDate.setOnClickListener { marsViewModel.resetDate() }
        marsViewModel.sendServerRequest()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: MarsViewModel.RequestState) {
        when (state) {
            is MarsViewModel.RequestState.Error -> {
                binding.imageView.setImageResource(R.drawable.ic_load_error_vector)
                binding.description.setText(state.error)
            }
            is MarsViewModel.RequestState.Loading -> {
                binding.imageView.setImageResource(R.drawable.bg_system)
                binding.description.setText(R.string.label_description_loading)
            }
            is MarsViewModel.RequestState.Success -> {
                var photos = state.response.photos
                if (photos.size > 0) {
                    val photo = photos[0]
                    binding.imageView.load(photo.url) {}
                    binding.imageView.visibility = View.VISIBLE
                    val cameraName = photo.camera?.fullName ?: ""
                    binding.description.setText("${photo.date} ${cameraName}")
                } else {
                    binding.imageView.setImageResource(R.drawable.ic_no_photo_vector)
                    binding.description.setText("No images for ${marsViewModel.nasaDate.format()}")
                }
            }
        }
    }
}