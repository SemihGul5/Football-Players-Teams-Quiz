package com.abrebo.playersteamsquiz.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.abrebo.playersteamsquiz.R
import com.abrebo.playersteamsquiz.data.model.GameCategory
import com.abrebo.playersteamsquiz.databinding.AdItemBinding
import com.abrebo.playersteamsquiz.databinding.GameCategoryItemBinding
import com.abrebo.playersteamsquiz.ui.fragment.HomeFragmentDirections
import com.abrebo.playersteamsquiz.ui.viewmodel.HomeViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout

class GameCategoryAdapter(
    val context: Context,
    private val categoryList: List<GameCategory>,
    private val viewModel: HomeViewModel,
    private val showProgressBar: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var adView: AdView
    companion object {
        private const val VIEW_TYPE_CATEGORY = 0
        private const val VIEW_TYPE_AD = 1
        private const val AD_POSITION = 3
    }

    inner class CategoryViewHolder(val binding: GameCategoryItemBinding) : RecyclerView.ViewHolder(binding.root)
    inner class AdViewHolder(val binding: AdItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CATEGORY) {
            val binding = GameCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
            CategoryViewHolder(binding)
        } else {
            val binding = AdItemBinding.inflate(LayoutInflater.from(context), parent, false)
            AdViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == AD_POSITION) VIEW_TYPE_AD else VIEW_TYPE_CATEGORY
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val categoryPosition = if (position < AD_POSITION) position else position - 1
            val category = categoryList[categoryPosition]
            val binding = holder.binding
            binding.kategoryTitle.text = category.title
            binding.rankButton.paintFlags =binding.rankButton.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            val easyGameId = category.id
            val mediumGameId = category.id + 100
            val hardGameId = category.id + 200


            binding.startGameButton.setOnClickListener {
                showProgressBar()
                val selectedTabPosition = binding.difficultyTabLayout.selectedTabPosition

                val gameId = when (selectedTabPosition) {
                    0 -> easyGameId
                    1 -> mediumGameId
                    2 -> hardGameId
                    else -> easyGameId
                }
                val navDirection = HomeFragmentDirections.actionHomeFragmentToGameFragment(gameId,category.title)
                Navigation.findNavController(it).navigate(navDirection)
            }
            binding.rankButton.setOnClickListener {
                val navDirection = HomeFragmentDirections.actionHomeFragmentToRankFragment(category.id)
                Navigation.findNavController(it).navigate(navDirection)
            }


            binding.difficultyTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        0 -> {
                            // Kolay
                            binding.highestScoreText.text = context.getString(R.string.en_yuksek_skorum) +" "+ category.highestScore
                            binding.rankText.text=context.getString(R.string.siralama) +": "+category.rank.toString()
                        }
                        1 -> {
                            // Orta
                            binding.highestScoreText.text = context.getString(R.string.en_yuksek_skorum) +" "+ category.highestScore
                            binding.rankText.text=context.getString(R.string.siralama) +": "+category.rank.toString()
                        }
                        2 -> {
                            // Zor
                            binding.highestScoreText.text = context.getString(R.string.en_yuksek_skorum) +" "+ category.highestScore
                            binding.rankText.text=context.getString(R.string.siralama) +": "+category.rank.toString()
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })

        } else if (holder is AdViewHolder) {
            val binding=holder.binding
            MobileAds.initialize(context) {}

            // Setup Banner Ad
            adView = AdView(context)
            adView.adUnitId = "ca-app-pub-4667560937795938/3637944447"
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE)
            binding.adView.removeAllViews()
            binding.adView.addView(adView)

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        }
    }
}
