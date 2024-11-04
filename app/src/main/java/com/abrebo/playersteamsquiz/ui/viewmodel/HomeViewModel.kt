package com.abrebo.playersteamsquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.playersteamsquiz.R
import com.abrebo.playersteamsquiz.data.model.GameCategory
import com.abrebo.playersteamsquiz.data.repo.Repository
import com.abrebo.playersteamsquiz.datastore.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (var repository: Repository,
                                         application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    val highestScore = MutableLiveData<Int>()
    val categoryList = MutableLiveData<List<GameCategory>>()


    fun getHighestScore(userName: String,game:Int) {
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userName,game)
            highestScore.value = currentHighestScore
        }
    }

    fun getUserName(onResult: (String?) -> Unit){
        viewModelScope.launch {
            val appPref=AppPref.getInstance(context)
            viewModelScope.launch {
                onResult(appPref.getUserName())
                Log.e("UserName:",appPref.getUserName())
            }
        }
    }
    fun loadCategories(userName: String) {
        val categories = listOf(
            GameCategory(1, context.getString(R.string.FindthePlayersPhoto)),
            GameCategory(2, context.getString(R.string.FindtheTeamPhoto)),
            GameCategory(3, context.getString(R.string.FindthePlayerNamefromthePhoto)),
            GameCategory(6, context.getString(R.string.FootballPlayerCountryGuessingGame)),
            GameCategory(4, context.getString(R.string.FootballPlayerOverallGuessingGame)),
            GameCategory(5, context.getString(R.string.FootballPlayerMarketValueGuessingGame))

        )

        categoryList.value = categories

        categories.forEach { category ->
            viewModelScope.launch {
                val easyGameId = category.id
                val mediumGameId = category.id + 100
                val hardGameId = category.id + 200

                category.scores["easy"] = repository.getHighestScore(userName, easyGameId)
                category.ranks["easy"] = repository.getAllRankUsers(easyGameId)
                    .find { it.userName == userName }?.rank ?: 0

                category.scores["medium"] = repository.getHighestScore(userName, mediumGameId)
                category.ranks["medium"] = repository.getAllRankUsers(mediumGameId)
                    .find { it.userName == userName }?.rank ?: 0

                category.scores["hard"] = repository.getHighestScore(userName, hardGameId)
                category.ranks["hard"] = repository.getAllRankUsers(hardGameId)
                    .find { it.userName == userName }?.rank ?: 0

                categoryList.postValue(categories)
            }
        }
    }


}