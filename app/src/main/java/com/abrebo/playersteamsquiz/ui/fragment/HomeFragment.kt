package com.abrebo.playersteamsquiz.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.abrebo.playersteamsquiz.R
import com.abrebo.playersteamsquiz.databinding.FragmentHomeBinding
import com.abrebo.playersteamsquiz.ui.adapter.GameCategoryAdapter
import com.abrebo.playersteamsquiz.ui.viewmodel.HomeViewModel
import com.abrebo.playersteamsquiz.utils.BackPressUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.pacifico)
        binding.textView.typeface = typeface
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/1298721563"
        adView.setAdSize(AdSize.BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BackPressUtils.setBackPressCallback(this, viewLifecycleOwner)


        viewModel.categoryList.observe(viewLifecycleOwner) { categoryList ->
            val adapter = GameCategoryAdapter(requireContext(), categoryList, viewModel){
                showProgressBar()
            }
            binding.recyclerViewGameCategory.adapter = adapter
        }



    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }
    private fun showProgressBar() {
        binding.progressBarHome.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBarHome.visibility = View.GONE
    }
    private fun initViewModel() {
        viewModel.getUserName() { userName ->
            if (userName != null) {
                viewModel.loadCategories(userName)
                viewModel.getHighestScore(userName, 1)
                viewModel.getHighestScore(userName, 2)
                viewModel.getHighestScore(userName, 3)
                viewModel.getHighestScore(userName, 4)
                viewModel.getHighestScore(userName, 5)
                viewModel.getHighestScore(userName, 6)
            }
        }
    }
}
