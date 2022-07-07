package com.example.prapp.ui.nointernet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.prapp.App
import com.example.prapp.R
import com.example.prapp.databinding.FragmentNoInternetBinding
import com.example.prapp.repository.NetworkStateRepo
import com.example.prapp.ui.BaseViewModel
import javax.inject.Inject

class NoInternetFragment: Fragment() {

    private var _binding: FragmentNoInternetBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val navController by lazy { binding.root.findNavController() }
    @Inject lateinit var vm: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).getDaggerComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoInternetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            vm.networkState.collect{
                when(it){
                    is NetworkStateRepo.NetworkState.Connected -> {
                        navController.navigate(R.id.action_noInternetFragment_to_webViewFragment)
                    }
                    is NetworkStateRepo.NetworkState.Disconnected -> {

                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}