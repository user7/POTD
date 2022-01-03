package com.geekbrains.potd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentMainBinding
import com.geekbrains.potd.fragments.earth.EarthFragment
import com.geekbrains.potd.fragments.mars.MarsFragment
import com.geekbrains.potd.fragments.system.SystemFragment

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding : FragmentMainBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val navFragments = mapOf(
        Pair(R.id.bottom_view_system, SystemFragment()),
        Pair(R.id.bottom_view_earth, EarthFragment()),
        Pair(R.id.bottom_view_mars, MarsFragment()),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener {
            navFragments[it.itemId]?.let { fragment ->
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.photosContainer, fragment)
                    .commit()
                true
            } ?: false
        }
        binding.bottomNavigationView.selectedItemId = R.id.bottom_view_system
        super.onViewCreated(view, savedInstanceState)
    }
}