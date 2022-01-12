package com.geekbrains.potd.fragments.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.load
import com.geekbrains.potd.MainViewModel
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.FragmentSystemBinding
import com.geekbrains.potd.fragments.bookmarks.Bookmark

class SystemFragment : Fragment() {
    private var _binding: FragmentSystemBinding? = null
    val binding: FragmentSystemBinding get() = _binding!!

    private val fragmentViewModel: SystemViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

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

    private var imageViewExpanded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewModel.state.observe(viewLifecycleOwner) { renderState(it) }
        binding.chipNextDay.setOnClickListener { fragmentViewModel.adjustDayShift(1) }
        binding.chipPrevDay.setOnClickListener { fragmentViewModel.adjustDayShift(-1) }
        binding.chipResetDate.setOnClickListener { fragmentViewModel.resetDayShift() }
        binding.chipHD.setOnClickListener { fragmentViewModel.adjustDayShift(0) }
        fragmentViewModel.sendServerRequest()
        binding.imageView.setOnClickListener { imageViewResize() }
        binding.bookmarkCheckbox.setOnCheckedChangeListener { _, on -> handleBookmark(on) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun renderState(state: SystemViewModel.RequestState) {
        when (state) {
            is SystemViewModel.RequestState.Error -> {
                binding.imageView.setImageResource(R.drawable.ic_load_error_vector)
                binding.description.text = state.error
            }
            is SystemViewModel.RequestState.Loading -> {
                binding.imageView.setImageResource(R.drawable.bg_system)
                binding.description.setText(R.string.label_description_loading)
                binding.bookmarkCheckbox.visibility = View.GONE
            }
            is SystemViewModel.RequestState.Success -> {
                val url = if (binding.chipHD.isChecked) state.response.hdurl else state.response.url
                binding.imageView.load(url) {}
                binding.description.text = state.response.title
                binding.bookmarkCheckbox.isChecked = isBookmarkSet()
                binding.bookmarkCheckbox.visibility = View.VISIBLE
            }
        }
    }

    private fun imageViewResize() {
        TransitionManager.beginDelayedTransition(
            binding.systemConstraintLayout,
            TransitionSet()
                .addTransition(ChangeBounds().apply { duration = 500 })
                .addTransition(ChangeImageTransform().apply { duration = 500 })
        )
        imageViewExpanded = !imageViewExpanded
        val params = binding.imageView.layoutParams
        if (imageViewExpanded) {
            binding.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            params.height = FrameLayout.LayoutParams.MATCH_PARENT
        } else {
            binding.imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            params.height = FrameLayout.LayoutParams.WRAP_CONTENT
        }
    }

    private fun makeBookmark(): Bookmark.Potd? =
        (fragmentViewModel.state.value as? SystemViewModel.RequestState.Success)?.let { state ->
            Bookmark.Potd(state.response, fragmentViewModel.nasaDate.format())
        }

    private fun handleBookmark(add: Boolean) =
        makeBookmark()?.let { bookmark -> activityViewModel.editBookmark(bookmark, add) }

    private fun isBookmarkSet() =
        makeBookmark()?.let { activityViewModel.isBookmarkPresent(it) } ?: false
}