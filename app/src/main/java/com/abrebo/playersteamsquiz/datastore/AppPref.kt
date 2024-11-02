package com.abrebo.playersteamsquiz.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class AppPref(var context:Context) {
    val Context.ds: DataStore<Preferences> by preferencesDataStore("bilgiler")

    companion object {
        val USERNAME_KEY = stringPreferencesKey("UserName")
    }

    suspend fun saveUserName(userName:String){
        context.ds.edit {
            it[USERNAME_KEY]=userName
        }
    }
    suspend fun getUserName():String{
        val p=context.ds.data.first()
        return p[USERNAME_KEY]?:"No have a UserName"
    }
}