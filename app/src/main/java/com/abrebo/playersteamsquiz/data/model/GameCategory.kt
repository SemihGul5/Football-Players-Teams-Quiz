package com.abrebo.playersteamsquiz.data.model

data class GameCategory(
    val id: Int,
    val title: String,
    val scores: MutableMap<String, Int> = mutableMapOf(),
    val ranks: MutableMap<String, Int> = mutableMapOf()
)

