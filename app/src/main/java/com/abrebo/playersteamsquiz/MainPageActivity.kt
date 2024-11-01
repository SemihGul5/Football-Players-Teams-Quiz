package com.abrebo.playersteamsquiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abrebo.playersteamsquiz.databinding.ActivityMainPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainPageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}