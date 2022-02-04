package com.geekbrains.potd

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.potd.databinding.ActivityMainBinding
import com.geekbrains.potd.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private val handler: Handler by lazy { Handler(mainLooper) }
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.star1.animate().rotationBy(100f).duration = 2100
        binding.star2.animate().rotationBy(-100f).duration = 2100
        binding.star3.animate().rotationBy(100f).duration = 2100

        handler.postDelayed(
            {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            },
            2000
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

}