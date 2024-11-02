package com.abrebo.playersteamsquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.playersteamsquiz.data.model.RankUser
import com.abrebo.playersteamsquiz.data.repo.Repository
import com.abrebo.playersteamsquiz.datastore.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor (var repository: Repository,
                                         application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    var userRankList = MutableLiveData<List<RankUser>>()
    fun getAllRankUsers(game:Int) {
        viewModelScope.launch {
            val users = repository.getAllRankUsers(game)
            userRankList.value = users
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


}