package com.abrebo.playersteamsquiz.data.model

data class PlayerQuestion(val id:Int,
                          val player_name:String,
                          val player_image_url:Int,
                          val team_name:String,
                          val country_name:String,
                          val age:String,
                          val overall:String,
                          val market_value:String,
                          val options:List<Any>) {
}