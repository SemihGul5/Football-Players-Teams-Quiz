package com.abrebo.playersteamsquiz.retrofit

class ApiUtils {
    companion object{
        val BASE_URL="https://raw.githubusercontent.com/SemihGul5/Sofifa-Data-Fetcher/refs/heads/main/"
        fun getTeamDao():QuestionDao{
            return  RetrofitClient.getClient(BASE_URL).create(QuestionDao::class.java)
        }
    }
}