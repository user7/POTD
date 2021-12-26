package com.geekbrains.potd.view.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.potd.databinding.ActivityApiBinding

class ApiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        for (i: Int in 0 until adapter.count) {
            adapter.getLayoutId(i)?.let {
                binding.tabLayout.getTabAt(i)?.customView = layoutInflater.inflate(it, null)
            }
        }
    }
}