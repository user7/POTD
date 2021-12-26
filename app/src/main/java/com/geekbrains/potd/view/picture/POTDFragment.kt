package com.geekbrains.potd.view.picture

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentMainBinding
import com.geekbrains.potd.view.ChangeThemeCallback
import com.geekbrains.potd.view.MainActivity
import com.geekbrains.potd.viewmodel.POTDState
import com.geekbrains.potd.viewmodel.POTDViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class POTDFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    val binding: FragmentMainBinding get() = _binding!!

    val viewModel: POTDViewModel by activityViewModels()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.sendServerRequest()
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        binding.chipToday.setOnClickListener { viewModel.setDayShift(0) }
        binding.chipYesterday.setOnClickListener { viewModel.setDayShift(-1) }
        binding.chipYesterday2.setOnClickListener { viewModel.setDayShift(-2) }
        setBottomAppBar()
    }

    private fun renderData(state: POTDState) {
        when (state) {
            is POTDState.Error -> {
            } //TODO
            is POTDState.Loading -> {
            } //TODO
            is POTDState.Success -> {
                val url = state.potdResponse.url
                binding.imageView.load(url) {}
                binding.imageDescription.setText(state.potdResponse.title)
                binding.includeBottomSheet.bottomSheetDescriptionHeader.text =
                    state.potdResponse.explanation
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var changeThemeCallback: ChangeThemeCallback
    override fun onAttach(context: Context) {
        changeThemeCallback = context as ChangeThemeCallback
        super.onAttach(context)
    }

    companion object {
        fun newInstance() = POTDFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme_steel -> changeThemeCallback.onChangeTheme(R.style.Theme_Steel)
            R.id.theme_copper -> changeThemeCallback.onChangeTheme(R.style.Theme_Copper)
            R.id.app_bar_settings -> {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, ChipsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            android.R.id.home -> BottomNavFragment().show(requireActivity().supportFragmentManager,
                "")
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private var isCollapsed = true
    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            val behavior = BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheetContainer)
            if (isCollapsed) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
            isCollapsed = !isCollapsed
        }
    }
}