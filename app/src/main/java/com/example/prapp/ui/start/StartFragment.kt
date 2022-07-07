package com.example.prapp.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.prapp.App
import com.example.prapp.R
import com.example.prapp.databinding.FragmentStartBinding
import com.example.prapp.utils.SPCache
import javax.inject.Inject

class StartFragment: Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val navController by lazy { binding.root.findNavController() }
    @Inject lateinit var sp: SPCache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).getDaggerComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkBox.setOnCheckedChangeListener { _, b ->
            when(b){
                true -> {binding.buttonAccept.isEnabled = true}
                else -> {binding.buttonAccept.isEnabled = false}
            }
        }

        binding.buttonAccept.setOnClickListener {
            sp.isFirstLaunch = false
            navController.navigate(R.id.action_startFragment_to_webViewFragment)
        }

        binding.buttonExit.setOnClickListener {
            requireActivity().finish()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}