package com.abrebo.playersteamsquiz.data.datasource

import android.util.Log
import com.abrebo.playersteamsquiz.data.model.PlayerQuestion
import com.abrebo.playersteamsquiz.data.model.RankUser
import com.abrebo.playersteamsquiz.data.model.TeamQuestion
import com.abrebo.playersteamsquiz.retrofit.QuestionDao
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GameDataSource(
    private val dao:QuestionDao,
    private val collectionReferenceGame1Easy:CollectionReference,
    private val collectionReferenceGame1Medium:CollectionReference,
    private val collectionReferenceGame1Hard:CollectionReference,
    private val collectionReferenceGame2Easy:CollectionReference,
    private val collectionReferenceGame2Medium:CollectionReference,
    private val collectionReferenceGame2Hard:CollectionReference,) {

    suspend fun getPlayers():List<PlayerQuestion> =
        withContext(Dispatchers.IO){
            return@withContext dao.getPlayers()
        }

    suspend fun getTeams(): List<TeamQuestion> =
        withContext(Dispatchers.IO){
            return@withContext dao.getTeams()
        }

    suspend fun getHighestScore(userId: String, game: Int): Int {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1Easy
            101 -> collectionReferenceGame1Medium
            201 -> collectionReferenceGame1Hard
            2 -> collectionReferenceGame1Medium
            3 -> collectionReferenceGame1Hard
            4 -> collectionReferenceGame2Easy
            5 -> collectionReferenceGame2Medium
            6 -> collectionReferenceGame2Hard
            7 -> collectionReferenceGame2Hard
            8 -> collectionReferenceGame2Hard
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        val document = collectionReference.document(userId).get().await()
        return document.getLong("highestScore")?.toInt() ?: 0
    }


    suspend fun saveHighestScore(userId: String, score: Int,game:Int) {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1Easy
            101 -> collectionReferenceGame1Medium
            201 -> collectionReferenceGame1Hard
            2 -> collectionReferenceGame1Medium
            3 -> collectionReferenceGame1Hard
            4 -> collectionReferenceGame2Easy
            5 -> collectionReferenceGame2Medium
            6 -> collectionReferenceGame2Hard
            7 -> collectionReferenceGame2Hard
            8 -> collectionReferenceGame2Hard
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        val userDoc = collectionReference.document(userId).get().await()
        if (!userDoc.exists()) {
            val rankUser = RankUser(
                rank = 0,
                userName = userId,
                highestScore = score
            )
            collectionReference.document(userId).set(rankUser)
            updateUserRanks(game)
        } else {
            val currentHighestScore = userDoc.getLong("highestScore")?.toInt() ?: 0
            if (score >= currentHighestScore) {
                val rankUser = RankUser(
                    rank = userDoc.getLong("rank")?.toInt() ?: 0,
                    userName = userId,
                    highestScore = score
                )
                collectionReference.document(userId).set(rankUser)

                updateUserRanks(game)
            }
        }
    }

    private suspend fun updateUserRanks(game:Int) {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1Easy
            101 -> collectionReferenceGame1Medium
            201 -> collectionReferenceGame1Hard
            2 -> collectionReferenceGame1Medium
            3 -> collectionReferenceGame1Hard
            4 -> collectionReferenceGame2Easy
            5 -> collectionReferenceGame2Medium
            6 -> collectionReferenceGame2Hard
            7 -> collectionReferenceGame2Hard
            8 -> collectionReferenceGame2Hard
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        val users = collectionReference.get().await().documents
        val sortedUsers = users.map { doc ->
            RankUser(
                rank = 0,
                userName = doc.id,
                highestScore = doc.getLong("highestScore")?.toInt() ?: 0
            )
        }.sortedByDescending { it.highestScore }
        sortedUsers.forEachIndexed { index, rankUser ->
            collectionReference.document(rankUser.userName).update("rank", index + 1)
        }
    }


    suspend fun getAllRankUsers(game:Int): List<RankUser> {
        val collectionReference = when (game) {
            1 -> collectionReferenceGame1Easy
            101 -> collectionReferenceGame1Medium
            201 -> collectionReferenceGame1Hard
            2 -> collectionReferenceGame1Medium
            3 -> collectionReferenceGame1Hard
            4 -> collectionReferenceGame2Easy
            5 -> collectionReferenceGame2Medium
            6 -> collectionReferenceGame2Hard
            7 -> collectionReferenceGame2Hard
            8 -> collectionReferenceGame2Hard
            else -> throw IllegalArgumentException("Geçersiz oyun numarası: $game")
        }
        return try {
            val usersSnapshot = collectionReference.get().await()
            usersSnapshot.documents.map { document ->
                RankUser(
                    rank = document.getLong("rank")?.toInt() ?: 0,
                    userName = document.getString("userName") ?: "Unknown",
                    highestScore = document.getLong("highestScore")?.toInt() ?: 0
                )
            }.sortedBy { it.rank }
        } catch (e: Exception) {
            Log.e("hata",e.message.toString())
            emptyList()
        }
    }
}