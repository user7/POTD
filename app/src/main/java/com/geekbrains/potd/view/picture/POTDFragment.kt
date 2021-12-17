package com.geekbrains.potd.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentMainBinding
import com.geekbrains.potd.view.MainActivity
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
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
        setBottomAppBar()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            android.R.id.home -> BottomNavFragment().show(requireActivity().supportFragmentManager, "")
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
    }
}