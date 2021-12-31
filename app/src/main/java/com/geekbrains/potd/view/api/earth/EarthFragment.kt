package com.geekbrains.potd.view.api.earth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentEarthBinding
import com.geekbrains.potd.databinding.FragmentSystemBinding
import com.geekbrains.potd.repository.EpicPhotosDTO

class EarthFragment : Fragment() {
    private var _binding: FragmentEarthBinding? = null
    val binding: FragmentEarthBinding get() = _binding!!

    val earthViewModel: EarthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        earthViewModel.state.observe(viewLifecycleOwner) { renderState(it) }
        binding.chipNextDay.setOnClickListener { earthViewModel.adjustDate(1) }
        binding.chipPrevDay.setOnClickListener { earthViewModel.adjustDate(-1) }
        binding.chipResetDate.setOnClickListener { earthViewModel.resetDate() }
        earthViewModel.sendServerRequest()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: EarthViewModel.RequestState) {
        when (state) {
            is EarthViewModel.RequestState.Error -> {
                binding.description.setText(state.error)
            }
            is EarthViewModel.RequestState.Loading -> {
                binding.recyclerView.adapter = null
                binding.description.setText(R.string.label_description_loading)
            }
            is EarthViewModel.RequestState.Success -> {
                val photos = state.response
                if (photos.size == 0) {
                    binding.description.setText("No photos for ${earthViewModel.nasaDate.format()}")
                } else {
                    binding.recyclerView.adapter = EarthPhotosAdapter(photos)
                    binding.description.setText("")
                }
            }
        }
    }
}