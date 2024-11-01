package com.abrebo.playersteamsquiz.data.model

data class GameCategory(
    val id:Int,
    val title: String,
    var highestScore: Int = 0,
    var rank:Int=0
)
