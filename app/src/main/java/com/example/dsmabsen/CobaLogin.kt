package com.example.dsmabsen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.dsmabsen.databinding.ActivityCobaLoginBinding
import com.example.dsmabsen.databinding.ActivityLoginBinding

class CobaLogin : AppCompatActivity() {
    private lateinit var binding: ActivityCobaLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCobaLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.frame8.background = ContextCompat.getDrawable(this, R.drawable.bg_white_input)
        binding.frame8.setOnFocusChangeListener { view, b ->
            binding.frame8.background = ContextCompat.getDrawable(this, R.drawable.bg_input_focus)
        }
    }
}