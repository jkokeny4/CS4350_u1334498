package com.example.assignment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedText = arguments?.getString(ARG_SELECTED_TEXT) ?: ""

        val selectedTextView = view.findViewById<TextView>(R.id.selected_text)
        selectedTextView.text = "You selected: $selectedText"

        val backButton = view.findViewById<Button>(R.id.back_button)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_SELECTED_TEXT = "selected_text"

        fun newInstance(selectedText: String): SecondFragment {
            val fragment = SecondFragment()

            fragment.arguments = Bundle().apply {
                putString(ARG_SELECTED_TEXT, selectedText)
            }

            return fragment
        }
    }
}