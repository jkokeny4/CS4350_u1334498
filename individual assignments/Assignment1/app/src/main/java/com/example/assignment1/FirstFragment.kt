package com.example.assignment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton(view, R.id.button_one)
        setupButton(view, R.id.button_two)
        setupButton(view, R.id.button_three)
        setupButton(view, R.id.button_four)
        setupButton(view, R.id.button_five)
    }

    private fun setupButton(view: View, buttonId: Int) {
        val button = view.findViewById<Button>(buttonId)

        button.setOnClickListener {
            openSecondFragment(button.text.toString())
        }
    }

    private fun openSecondFragment(selectedText: String) {
        val secondFragment = SecondFragment.newInstance(selectedText)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .addToBackStack(null)
            .commit()
    }
}