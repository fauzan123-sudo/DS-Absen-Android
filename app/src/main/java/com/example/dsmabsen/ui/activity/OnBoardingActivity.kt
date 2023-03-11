package com.example.dsmabsen.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dsmabsen.R
import com.example.dsmabsen.databinding.ActivityLoginBinding
import com.example.dsmabsen.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}