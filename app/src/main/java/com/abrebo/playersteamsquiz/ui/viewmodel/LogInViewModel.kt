package com.abrebo.playersteamsquiz.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.abrebo.playersteamsquiz.data.model.User
import com.abrebo.playersteamsquiz.data.repo.Repository
import com.abrebo.playersteamsquiz.datastore.AppPref
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor (var repository: Repository,
                                          application: Application): AndroidViewModel(application){
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val appPref=AppPref.getInstance(context)
    var userName=MutableLiveData<String>()

    fun saveUserName(userName:String){
        viewModelScope.launch {
            appPref.saveUserName(userName)
            repository.saveUser(User("",userName))
        }
    }
    fun getUserName(){
        viewModelScope.launch {
           userName.value = appPref.getUserName()
        }
    }
    fun checkUserNameAvailability(userName: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isAvailable = withContext(Dispatchers.IO) {
                repository.checkUserNameAvailability(userName)
            }
            callback(isAvailable)
        }
    }
}