package com.example.prapp.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.prapp.App
import com.example.prapp.R
import com.example.prapp.databinding.FragmentWebViewBinding
import com.example.prapp.repository.NetworkStateRepo.NetworkState.*
import com.example.prapp.ui.BaseViewModel
import com.example.prapp.utils.SPCache
import javax.inject.Inject

class WebViewFragment: Fragment() {

    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = checkNotNull(_binding)
    private val navController by lazy { binding.root.findNavController() }
    @Inject lateinit var sp: SPCache
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
        _binding = FragmentWebViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sp.isFirstLaunch) navController.navigate(R.id.action_webViewFragment_to_startFragment)

        lifecycleScope.launchWhenStarted {
            vm.networkState.collect{
                when(it){
                    is Connected -> {
                        setUpWebView(savedInstanceState)
                    }
                    is Disconnected -> {
                        navController.navigate(R.id.action_webViewFragment_to_noInternetFragment)
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            binding.webView.run {
                if (canGoBack()){
                    goBack()
                } else{
                    this@WebViewFragment.requireActivity().finish()
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        sp.lastPage = binding.webView.url
    }

    private fun setUpWebView(savedInstanceState: Bundle?){
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.apply {
                javaScriptEnabled = true
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                domStorageEnabled = true
            }
            if (savedInstanceState == null) {
                loadUrl(sp.lastPage!!)
            } else{
                restoreState(savedInstanceState)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}