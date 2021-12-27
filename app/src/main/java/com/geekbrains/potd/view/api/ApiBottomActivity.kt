package com.geekbrains.potd.view.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.potd.R
import com.geekbrains.potd.databinding.ActivityApiBottomBinding
import com.geekbrains.potd.view.api.earth.EarthFragment
import com.geekbrains.potd.view.api.mars.MarsFragment
import com.geekbrains.potd.view.api.system.SystemFragment

class ApiBottomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApiBottomBinding
    private val navFragments = mapOf(
        Pair(R.id.bottom_view_system, SystemFragment()),
        Pair(R.id.bottom_view_earth, EarthFragment()),
        Pair(R.id.bottom_view_mars, MarsFragment()),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener {
            navFragments[it.itemId]?.let { fragment ->
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                true
            } ?: false
        }
        binding.bottomNavigationView.selectedItemId = R.id.bottom_view_system
    }
}