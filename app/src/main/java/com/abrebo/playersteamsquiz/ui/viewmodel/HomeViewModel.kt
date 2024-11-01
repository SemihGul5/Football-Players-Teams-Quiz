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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (var repository: Repository,
                                             application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    val highestScore = MutableLiveData<Int>()
    val categoryList = MutableLiveData<List<GameCategory>>()
    val isClicked = MutableLiveData<Boolean>(false)
    fun getHighestScore(userName: String,game:Int) {
        viewModelScope.launch {
            val currentHighestScore = repository.getHighestScore(userName,game)
            highestScore.value = currentHighestScore
        }
    }
    fun click() {
        isClicked.value = true
    }

    fun resetClick() {
        isClicked.value = false
    }

    fun getUserNameByEmail(userEmail: String, onResult: (String?) -> Unit){
        viewModelScope.launch {
            onResult(repository.getUserNameByEmail(userEmail))
        }
    }
    fun loadCategories(userName: String) {
        val categories = listOf(
            GameCategory(1, "Futbolcu Fotoğrafını bulun"),
            GameCategory(2, "Takım Fotoğrafını bulun"),
            GameCategory(3, "Futbolcu Fotoğrafından Adını Bulun"),
            GameCategory(4, "Takım Fotoğrafından Adını Bulun"),
            GameCategory(5, "Futbolcu Overall Tahmin Oyunu"),
            GameCategory(6, "Takım Overall Tahmin Oyunu"),
            GameCategory(7, "Futbolcu Market Değeri Tahmin Oyunu"),
            GameCategory(8, "Futbolcu Ülke Tahmin Oyunu"),
        )

        categoryList.value = categories

        categories.forEach { category ->
            viewModelScope.launch {
                val highestScore = repository.getHighestScore(userName, category.id)
                category.highestScore = highestScore

                val rankUsers = repository.getAllRankUsers(category.id)
                val userRank = rankUsers.find { it.userName == userName }?.rank ?: 0
                category.rank = userRank

                categoryList.postValue(categories)
            }
        }
    }

}