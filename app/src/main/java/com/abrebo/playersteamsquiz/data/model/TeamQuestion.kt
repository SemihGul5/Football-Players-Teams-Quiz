package com.abrebo.playersteamsquiz.data.model

data class TeamQuestion(val team_name:String,
                        val overall:String,
                        val league_name:String,
                        val team_image_url:Int,
                        val country_name:String,
                        val european_cup:String,
                        val stars:Double,
                        val options:List<Any>) {
}