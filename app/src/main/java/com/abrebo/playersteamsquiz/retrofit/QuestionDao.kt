package com.abrebo.playersteamsquiz.retrofit

import com.abrebo.playersteamsquiz.data.model.PlayerQuestion
import com.abrebo.playersteamsquiz.data.model.TeamQuestion
import retrofit2.http.GET

interface QuestionDao {
    @GET("players.json")
    suspend fun getPlayers():List<PlayerQuestion>

    @GET("teams_sorted.json")
    suspend fun getTeams():List<TeamQuestion>
}