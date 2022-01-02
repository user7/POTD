package com.geekbrains.potd.fragments.mars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.PagerSnapHelper
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentMarsBinding

class MarsFragment : Fragment() {
    private var _binding: FragmentMarsBinding? = null
    val binding: FragmentMarsBinding get() = _binding!!
    val adapter = MarsPhotosAdapter()

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
        binding.recyclerView.adapter = adapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerView)
        marsViewModel.sendServerRequest()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: MarsViewModel.RequestState) {
        when (state) {
            is MarsViewModel.RequestState.Error -> {
                adapter.setData(null)
                binding.description.text = state.error
            }
            is MarsViewModel.RequestState.Loading -> {
                adapter.setData(null)
                binding.description.setText(R.string.label_description_loading)
            }
            is MarsViewModel.RequestState.Success -> {
                adapter.setData(state.response)
                val label = if (state.response.photos.isEmpty())
                    "No photos for ${marsViewModel.nasaDate.format()}" else ""
                binding.description.text = label
            }
        }
    }
}