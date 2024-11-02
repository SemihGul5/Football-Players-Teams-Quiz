package com.abrebo.playersteamsquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.playersteamsquiz.data.model.PlayerQuestion
import com.abrebo.playersteamsquiz.data.model.TeamQuestion
import com.abrebo.playersteamsquiz.data.repo.Repository
import com.abrebo.playersteamsquiz.datastore.AppPref
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val _playerQuestion = MutableLiveData<PlayerQuestion>()
    val playerQuestion: LiveData<PlayerQuestion> get() = _playerQuestion

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score

    private val _teamQuestion = MutableLiveData<TeamQuestion>()
    val teamQuestion: LiveData<TeamQuestion> get() = _teamQuestion

    private val questionsPlayer = mutableListOf<PlayerQuestion>()
    private val questionsTeam = mutableListOf<TeamQuestion>()
    private var questionIndex = 0

    private val _highestScore = MutableLiveData<Int>()
    val highestScore: LiveData<Int> get() = _highestScore

    private var interstitialAd: InterstitialAd? = null

    private var teams = mutableListOf<TeamQuestion>()

    init {
        _score.value = 0

    }

    fun prepareQuestionsGame1(id: Int) {
        val allPlayers = repository.getAllPlayers()
        val playersToUse = when (id) {
            1 -> allPlayers.take(180)
            101 -> allPlayers.take(360)
            201 -> allPlayers
            else -> allPlayers.take(180)
        }

        val shuffledPlayers = playersToUse.shuffled()
        questionsPlayer.clear()

        shuffledPlayers.forEach { correctPlayer ->
            val options = shuffledPlayers
                .filter { it.id != correctPlayer.id }
                .shuffled()
                .take(3)
                .map { it.player_image_url }
                .toMutableList()

            options.add(Random.nextInt(4), correctPlayer.player_image_url)

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
            questionsPlayer.add(question)
        }
    }
    fun prepareQuestionsGame2(id: Int) {
        val allTeams = repository.getAllTeams()
        val teamsToUse = when (id) {
            2 -> allTeams.take(60)
            102 -> allTeams.take(120)
            202 -> allTeams
            else -> allTeams.take(60)
        }

        val shuffledTeams = teamsToUse.shuffled()
        questionsTeam.clear()

        shuffledTeams.forEach { correctTeam ->
            val options = shuffledTeams
                .filter { it.team_name != correctTeam.team_name }
                .shuffled()
                .take(3)
                .map { it.team_image_url }
                .toMutableList()

            options.add(Random.nextInt(4), correctTeam.team_image_url)

            val question = TeamQuestion(
                team_name = correctTeam.team_name,
                overall = correctTeam.overall,
                league_name = correctTeam.league_name,
                team_image_url = correctTeam.team_image_url,
                country_name = correctTeam.country_name,
                european_cup = correctTeam.european_cup,
                stars = correctTeam.stars,
                options = options
            )
            questionsTeam.add(question)
        }
    }
    fun prepareQuestionsGame3(id: Int) {
        val allPlayers = repository.getAllPlayers()
        val playersToUse = when (id) {
            3 -> allPlayers.take(180)
            103 -> allPlayers.take(360)
            203 -> allPlayers
            else -> allPlayers.take(180)
        }

        val shuffledPlayers = playersToUse.shuffled()
        questionsPlayer.clear()

        shuffledPlayers.forEach { correctPlayer ->
            val options = shuffledPlayers
                .filter { it.id != correctPlayer.id }
                .shuffled()
                .take(3)
                .map { it.player_name }
                .toMutableList()

            options.add(Random.nextInt(4), correctPlayer.player_name)

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
            questionsPlayer.add(question)
        }
    }




    fun prepareQuestionsGame4(id: Int) {
        val allPlayers = repository.getAllPlayers()
        val playersToUse = when (id) {
            3 -> allPlayers.take(180)
            103 -> allPlayers.take(360)
            203 -> allPlayers
            else -> allPlayers.take(180)
        }

        val shuffledPlayers = playersToUse.shuffled()
        questionsPlayer.clear()

        shuffledPlayers.forEach { correctPlayer ->
            val options = shuffledPlayers
                .filter { it.id != correctPlayer.id && it.overall!=correctPlayer.overall }
                .distinctBy { it.overall }
                .shuffled()
                .take(3)
                .map { it.overall }
                .toMutableList()

            options.add(Random.nextInt(4), correctPlayer.overall)

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
            questionsPlayer.add(question)
        }
    }

    fun prepareQuestionsGame5(id: Int) {
        val allPlayers = repository.getAllPlayers()
        val playersToUse = when (id) {
            3 -> allPlayers.take(180)
            103 -> allPlayers.take(360)
            203 -> allPlayers
            else -> allPlayers.take(180)
        }

        val shuffledPlayers = playersToUse.shuffled()
        questionsPlayer.clear()

        shuffledPlayers.forEach { correctPlayer ->
            val options = shuffledPlayers
                .filter { it.id != correctPlayer.id && it.market_value!=correctPlayer.market_value }
                .distinctBy { it.market_value }
                .shuffled()
                .take(3)
                .map { it.market_value }
                .toMutableList()

            options.add(Random.nextInt(4), correctPlayer.market_value)

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
            questionsPlayer.add(question)
        }
    }
    fun prepareQuestionsGame6(id: Int) {
        val allPlayers = repository.getAllPlayers()
        val playersToUse = when (id) {
            3 -> allPlayers.take(180)
            103 -> allPlayers.take(360)
            203 -> allPlayers
            else -> allPlayers.take(180)
        }

        val shuffledPlayers = playersToUse.shuffled()
        questionsPlayer.clear()

        shuffledPlayers.forEach { correctPlayer ->
            val options = shuffledPlayers
                .filter { it.id != correctPlayer.id && it.country_name!=correctPlayer.country_name }
                .distinctBy { it.country_name }
                .shuffled()
                .take(3)
                .map { it.country_name }
                .toMutableList()

            options.add(Random.nextInt(4), correctPlayer.country_name)

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

            questionsPlayer.add(question)
        }
    }

    fun nextQuestion(id: Int) {
        if (questionIndex < questionsPlayer.size) {
            _playerQuestion.value = questionsPlayer[questionIndex]
            questionIndex++
        } else {
            resetGame(id)
        }
    }

    fun nextQuestionTeam(id: Int) {
        if (questionIndex < questionsTeam.size) {
            _teamQuestion.value = questionsTeam[questionIndex]
            questionIndex++
        } else {
            resetGame(id)
        }
    }

    fun checkAnswer(selectedDrawable: Int): Boolean {
        return if (selectedDrawable == _playerQuestion.value?.player_image_url) {
            _score.value = (_score.value ?: 0) + 1
            true
        } else {
            false
        }
    }
    fun checkAnswer(selected: String): Boolean {
        return if (selected == _playerQuestion.value?.player_name) {
            _score.value = (_score.value ?: 0) + 1
            true
        } else {
            false
        }
    }
    fun checkAnswerOverall(selected: String): Boolean {
        return if (selected == _playerQuestion.value?.overall) {
            _score.value = (_score.value ?: 0) + 1
            true
        } else {
            false
        }
    }
    fun checkAnswerMarketValue(selected: String): Boolean {
        return if (selected == _playerQuestion.value?.market_value) {
            _score.value = (_score.value ?: 0) + 1
            true
        } else {
            false
        }
    }
    fun checkAnswerCountry(selected: String): Boolean {
        return if (selected == _playerQuestion.value?.country_name) {
            _score.value = (_score.value ?: 0) + 1
            true
        } else {
            false
        }
    }
    fun checkAnswerTeam(selectedDrawable: Int): Boolean {
        return if (selectedDrawable == _teamQuestion.value?.team_image_url) {
            _score.value = (_score.value ?: 0) + 1
            true
        } else {
            false
        }
    }
    fun updateScore(newScore: Int, userId: String, game: Int) {
        _score.value = newScore
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userId, game)
            _highestScore.value = currentHighestScore

            if (newScore >= (currentHighestScore ?: 0)) {
                repository.saveHighestScore(userId, newScore, game)
            }
        }
    }

    fun getUserName(onResult: (String?) -> Unit){
        viewModelScope.launch {
            val appPref= AppPref.getInstance(context)
            viewModelScope.launch {
                onResult(appPref.getUserName())
                Log.e("UserName:",appPref.getUserName())
            }
        }
    }
    private fun resetQuestions() {
        questionIndex = 0
        questionsPlayer.clear()
        questionsTeam.clear()
        _score.value = 0
    }

    private fun resetGame(id: Int) {
        resetQuestions()

        when (id) {
            1, 101, 201 -> {
                prepareQuestionsGame1(id)
                nextQuestion(id)
            }
            2, 102, 202 -> {
                prepareQuestionsGame2(id)
                nextQuestionTeam(id)
            }
            3, 103, 203 -> {
                prepareQuestionsGame3(id)
                nextQuestion(id)
            }
            4, 104, 204 -> {
                prepareQuestionsGame4(id)
                nextQuestion(id)
            }
            5, 105, 205 -> {
                prepareQuestionsGame5(id)
                nextQuestion(id)
            }
            6, 106, 206 -> {
                prepareQuestionsGame6(id)
                nextQuestion(id)
            }
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
        interstitialAd?.show(activity)
    }
}
