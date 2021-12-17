package com.geekbrains.potd.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.potd.databinding.FragmentChipsBinding
import com.geekbrains.potd.viewmodel.POTDViewModel

class ChipsFragment : Fragment() {
    private var _binding: FragmentChipsBinding? = null
    val binding: FragmentChipsBinding get() = _binding!!

    private val viewModel: POTDViewModel by lazy {
        ViewModelProvider(this).get(POTDViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = ChipsFragment()
    }
}