package com.geekbrains.potd.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.geekbrains.potd.databinding.FragmentMainBinding
import com.geekbrains.potd.viewmodel.POTDState
import com.geekbrains.potd.viewmodel.POTDViewModel

class POTDFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!

    private val viewModel: POTDViewModel by lazy {
        ViewModelProvider(this).get(POTDViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.sendServerRequest()
    }

    private fun renderData(state: POTDState) {
        when(state) {
            is POTDState.Error -> {} //TODO
            is POTDState.Loading -> {} //TODO
            is POTDState.Success -> {
                val url = state.potdResponse.url
                binding.imageView.load(url) {}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = POTDFragment()
    }
}