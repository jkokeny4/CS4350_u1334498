package com.example.emailsplitter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.emailsplitter.databinding.FragmentResultBinding

class ResultFragment : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentResultBinding.bind(view)

        val username = arguments?.getString(ARG_USERNAME)
        val domain = arguments?.getString(ARG_DOMAIN)

        binding.userView.text = username
        binding.domainView.text = domain
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_USERNAME = "username"
        private const val ARG_DOMAIN = "domain"

        fun newInstance(
            username: String,
            domain: String
        ): ResultFragment {

            val fragment = ResultFragment()

            val args = Bundle()
            args.putString(ARG_USERNAME, username)
            args.putString(ARG_DOMAIN, domain)

            fragment.arguments = args

            return fragment
        }
    }
}