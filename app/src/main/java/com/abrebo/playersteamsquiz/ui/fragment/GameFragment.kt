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
    private var title: String = ""
    private lateinit var adView: AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id=GameFragmentArgs.fromBundle(requireArguments()).id
        title=GameFragmentArgs.fromBundle(requireArguments()).title
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        initializeAds()
        viewModel.loadInterstitialAd()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.nextQuestion(id)
        binding.materialToolbar2.title= title
        setupProgressAndTimer(5,5,5000)
        when (id) {
            1,101,201-> {
                imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)
                viewModel.prepareQuestionsGame1(id)
            }
            2,102,202->{
                imageViews = listOf(binding.image1, binding.image2, binding.image3, binding.image4)
                viewModel.prepareQuestionsGame2(id)
            }
            3,103,203->{
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupOtherQuestionStyle()
                viewModel.prepareQuestionsGame3(id)
            }
            4,104,204->{
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupOtherQuestionStyle()
                binding.questionPlayerNameText.visibility=View.VISIBLE
                viewModel.prepareQuestionsGame4(id)
            }
            5,105,205->{
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupOtherQuestionStyle()
                binding.questionPlayerNameText.visibility=View.VISIBLE
                viewModel.prepareQuestionsGame5(id)
            }
            6,106,206->{
                answerButtons = listOf(binding.answer1, binding.answer2, binding.answer3, binding.answer4)
                setupOtherQuestionStyle()
                binding.questionPlayerNameText.visibility=View.VISIBLE
                viewModel.prepareQuestionsGame6(id)
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

    private fun setupOtherQuestionStyle() {
        binding.game1CountryNameText.visibility=View.GONE
        binding.game1LinearLayout.visibility=View.GONE
        binding.game2FlagImage.visibility=View.VISIBLE
        binding.game2LinearLayout.visibility=View.VISIBLE
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
        when (id){
            1,101,201->{
                viewModel.playerQuestion.observe(viewLifecycleOwner){question->
                    binding.game1CountryNameText.text = question.player_name
                    imageViews.forEachIndexed { index, imageView ->
                        imageView.setImageResource(question.options[index] as Int)
                        imageView.tag = question.options[index]
                    }
                }
            }
            2,102,202->{
                viewModel.teamQuestion.observe(viewLifecycleOwner){question->
                    binding.game1CountryNameText.text = question.team_name
                    imageViews.forEachIndexed { index, imageView ->
                        imageView.setImageResource(question.options[index] as Int)
                        imageView.tag = question.options[index]
                    }
                }
            }
            3,103,203,4,104,204,5,105,205,6,106,206->{
                viewModel.playerQuestion.observe(viewLifecycleOwner){question->
                    when (id){
                        4,104,204,5,105,205,6,106,206->{
                            binding.questionPlayerNameText.text=question.player_name
                        }
                    }
                    binding.game2FlagImage.setImageResource(question.player_image_url)
                    answerButtons.forEachIndexed { index, button ->
                        button.text = question.options[index].toString()
                        button.tag = question.options[index]
                    }
                }
            }
            //
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
                            resetTimer(5000, id)
                        }
                    }
                }
            }
            2,102,202 -> {
                imageViews.forEach { imageView ->
                    imageView.setOnClickListener { view ->
                        val selectedDrawable = view.tag as Int
                        if (!viewModel.checkAnswerTeam(selectedDrawable)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestionTeam(id)
                            resetTimer(5000, id)
                        }
                    }
                }
            }
            3,103,203->{
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selected = view.tag as String
                        if (!viewModel.checkAnswer(selected)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(5000,id)
                        }
                    }
                }
            }
            4,104,204 -> {
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selected = view.tag as String
                        if (!viewModel.checkAnswerOverall(selected)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestionTeam(id)
                            resetTimer(5000, id)
                        }
                    }
                }
            }
            5,105,205->{
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selected = view.tag as String
                        if (!viewModel.checkAnswerMarketValue(selected)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestion(id)
                            resetTimer(5000,id)
                        }
                    }
                }
            }

            6,106,206->{
                answerButtons.forEach { button ->
                    button.setOnClickListener { view ->
                        val selected = view.tag as String
                        if (!viewModel.checkAnswerCountry(selected)) {
                            showScoreDialog(id)
                        } else {
                            viewModel.nextQuestionTeam(id)
                            resetTimer(5000,id)
                        }
                    }
                }
            }
            //
        }
    }


    private fun startTimer(id:Int) {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                showScoreDialog(id)
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateTimer() {
        val secondsLeft = (timeLeftInMillis / 1000).toInt()
        binding.timerText.text =requireContext().getString(R.string.time)+secondsLeft
        binding.progressBar.progress = secondsLeft
        when {
            secondsLeft > 3 -> {
                val greenColor = ContextCompat.getColor(requireContext(), R.color.green)
                binding.timerText.setTextColor(greenColor)
                binding.progressBar.progressTintList = ColorStateList.valueOf(greenColor)
            }
            secondsLeft > 2 -> {
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
    private fun initializeAds() {
        MobileAds.initialize(requireContext()) {}
        adView = AdView(requireContext())
        adView.adUnitId = "ca-app-pub-4667560937795938/9000153581"
        adView.setAdSize(AdSize.LARGE_BANNER)
        binding.adView.removeAllViews()
        binding.adView.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}

