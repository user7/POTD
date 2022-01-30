package com.geekbrains.potd.fragments.bookmarks

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.geekbrains.potd.databinding.FragmentEditDialogBinding

class EditDialogFragment(
    private val title: String,
    private val initialText: String,
    private val editTextCallback: EditTextCallback,
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    interface EditTextCallback {
        fun submitEditedText(t: String)
    }

    private lateinit var binding: FragmentEditDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.titleTextView.text = title
        binding.textInputLayout.editText?.setText(initialText)
        binding.okButton.setOnClickListener {
            editTextCallback.submitEditedText(
                binding.textInputLayout.editText?.text.toString() ?: ""
            )
            dismiss()
        }
        binding.cancelButton.setOnClickListener { dismiss() }
        isCancelable = true
    }
}