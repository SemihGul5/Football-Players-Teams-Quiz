package com.abrebo.playersteamsquiz.ui.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.abrebo.playersteamsquiz.R
import com.abrebo.playersteamsquiz.databinding.FragmentGameBinding
import com.abrebo.playersteamsquiz.ui.viewmodel.QuizViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var imageViews: List<ImageView>
    private var timeLeftInMillis: Long = 60000
    private lateinit var countDownTimer: CountDownTimer
    private var isGameFinished = false
    private lateinit var answerButtons: List<Button>
    private var id: Int = 0
    private lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id=GameFragmentArgs.fromBundle(requireArguments()).id
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        MobileAds.initialize(requireContext()) {}

        // Setup Banner Ad
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/9000153581"
        adView.setAdSize(AdSize.LARGE_BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        viewModel.loadInterstitialAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.nextQuestion(id)

        when (id) {
            1,101,201-> {
                binding.materialToolbar2.title= "Futbolcunun Fotoğrafını Bulun $id"
                imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)
                viewModel.prepareQuestionsGame1(id)
                setupProgressAndTimer(10,10,10000)
            }


        }

        setupObservers(id)
        setupClickListeners(id)
        startTimer(id)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showScoreDialog(id)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        if (!isGameFinished) {
            countDownTimer.cancel()
            showScoreDialog(id)
        }
    }
    private fun setupProgressAndTimer(progress:Int,progressMax:Int,timeLeftInMilis:Long){
        binding.progressBar.progress=progress
        binding.progressBar.max=progressMax
        timeLeftInMillis=timeLeftInMilis
    }
    @SuppressLint("SetTextI18n")
    private fun setupObservers(id: Int) {
        viewModel.currentQuestion.observe(viewLifecycleOwner) { question ->
            when (id) {
                1,101,201 -> {
                    binding.game1CountryNameText.text = question.player_name
                    imageViews.forEachIndexed { index, imageView ->
                        imageView.setImageResource(question.options[index] as Int)
                        imageView.tag = question.options[index]
                    }
                }
            }
        }
    }

    private fun setupClickListeners(id: Int) {
        when (id) {
            1,101,201 -> {
                imageViews.forEach { imageView ->
                    imageView.setOnClickListener { view ->
                        val selectedDrawable = view.tag as Int
                        if (!viewModel.checkAnswer(selectedDrawable)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(10000, id)
                        }
                    }
                }
            }
        }
    }


    private fun startTimer(id:Int) {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer(id)
            }

            override fun onFinish() {
                showScoreDialog(id)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimer(id: Int) {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        binding.timerText.text =requireContext().getString(R.string.time)+secondsLeft
        binding.progressBar.progress = secondsLeft

        when (id) {
            20 -> {
                when {
                    secondsLeft > 40 -> {
                        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
                        binding.timerText.setTextColor(greenColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(greenColor)
                    }
                    secondsLeft > 20 -> {
                        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                        binding.timerText.setTextColor(yellowColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(yellowColor)
                    }
                    else -> {
                        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
                        binding.timerText.setTextColor(redColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(redColor)
                    }
                }
            }
            else -> {
                when {
                    secondsLeft > 7 -> {
                        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
                        binding.timerText.setTextColor(greenColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(greenColor)
                    }
                    secondsLeft > 3 -> {
                        val yellowColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                        binding.timerText.setTextColor(yellowColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(yellowColor)
                    }
                    else -> {
                        val redColor = ContextCompat.getColor(requireContext(), R.color.red)
                        binding.timerText.setTextColor(redColor)
                        binding.progressBar.progressTintList = ColorStateList.valueOf(redColor)
                    }
                }
            }
        }
    }

    private fun resetTimer(newTimeInMillis: Long,id:Int) {
        countDownTimer.cancel()
        timeLeftInMillis = newTimeInMillis
        startTimer(id)
    }
    private fun showScoreDialog(id:Int) {
        if (isGameFinished) return
        countDownTimer.cancel()
        isGameFinished = true
        val score = viewModel.score.value ?: 0
        val auth=FirebaseAuth.getInstance()
        val email=auth.currentUser?.email!!
        viewModel.getUserNameByEmail(email){
            if (it!=null){
                viewModel.updateScore(score,it.toString(),id)
            }
        }
        AlertDialog.Builder(requireContext())
            .setTitle(requireContext().getString(R.string.oyun_bitti))
            .setMessage(requireContext().getString(R.string.skorunuz)+score)
            .setPositiveButton(requireContext().getString(R.string.tamam)) { _, _ ->
                //viewModel.showInterstitialAd(requireActivity())
                Navigation.findNavController(binding.root).navigate(R.id.action_gameFragment_to_homeFragment)
            }
            .setCancelable(false)
            .show()
    }
    override fun onResume() {
        super.onResume()
        (activity as? HomeFragment)?.hideProgressBar()
    }
}

