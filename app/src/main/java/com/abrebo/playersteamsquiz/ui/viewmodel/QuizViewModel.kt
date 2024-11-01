package com.abrebo.playersteamsquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.playersteamsquiz.data.model.PlayerQuestion
import com.abrebo.playersteamsquiz.data.repo.Repository
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor (var repository: Repository, application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val _currentQuestion = MutableLiveData<PlayerQuestion>()
    val currentQuestion: LiveData<PlayerQuestion> get() = _currentQuestion
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score
    private val questions = mutableListOf<PlayerQuestion>()
    private var questionIndex = 0
    private val _highestScore = MutableLiveData<Int>()
    private var interstitialAd: InterstitialAd? = null
    val highestScore: LiveData<Int> get() = _highestScore
    init {
        _score.value = 0
    }

    fun prepareQuestionsGame1() {
        val allPlayers = repository.getAllPlayers()
        val shuffledPlayers = allPlayers.shuffled()
        shuffledPlayers.forEach { correctPlayer ->
            val options = shuffledPlayers
                .filter { it.id != correctPlayer.id }
                .shuffled()
                .take(3)
                .map { it.player_image_url }
                .toMutableList()

            val correctAnswerPosition = Random.nextInt(4)
            options.add(correctAnswerPosition, correctPlayer.player_image_url)

            val question = PlayerQuestion(
                id = correctPlayer.id,
                player_name = correctPlayer.player_name,
                player_image_url = correctPlayer.player_image_url,
                team_name = correctPlayer.team_name,
                country_name = correctPlayer.country_name,
                age = correctPlayer.age,
                overall = correctPlayer.overall,
                market_value = correctPlayer.market_value,
                options = options
            )
            questions.add(question)
        }
    }


    fun nextQuestion(id:Int) {
        if (questionIndex < questions.size) {
            _currentQuestion.value = questions[questionIndex]
            questionIndex++
        } else {
            resetGame(id)
        }
    }

    fun checkAnswer(selectedDrawable: Int): Boolean {
        return if (selectedDrawable == _currentQuestion.value?.player_image_url) {
            _score.value = _score.value?.plus(1)
            true
        } else {
            false
        }
    }


    fun updateScore(newScore: Int, userId: String,game:Int) {
        _score.value = newScore
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userId,game)
            _highestScore.value = currentHighestScore

            if (newScore >= currentHighestScore) {
                repository.saveHighestScore(userId, newScore,game)
            }
        }
    }
    fun getUserNameByEmail(userEmail: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            onResult(repository.getUserNameByEmail(userEmail))
        }
    }
    private fun resetGame(id:Int) {
        questionIndex = 0
        _score.value = 0
        questions.clear()
        /*when (id) {
            1 -> {
                prepareQuestionsGame1()
            }
        }*/
        viewModelScope.launch {
            prepareQuestionsGame1()
            nextQuestion(id) // Soruları yeniden hazırladıktan sonra ilk soruyu yükleyin.
        }

    }

    fun loadInterstitialAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, "ca-app-pub-4667560937795938/6264107780", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }
            })
    }
    fun showInterstitialAd(activity: Activity) {
        if (interstitialAd!=null){
            interstitialAd?.show(activity)
        }
    }

}