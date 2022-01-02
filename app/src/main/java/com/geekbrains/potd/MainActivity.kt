package com.geekbrains.potd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geekbrains.potd.databinding.ActivityMainBinding
import com.geekbrains.potd.fragments.earth.EarthFragment
import com.geekbrains.potd.fragments.mars.MarsFragment
import com.geekbrains.potd.fragments.system.SystemFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val navFragments = mapOf(
        Pair(R.id.bottom_view_system, SystemFragment()),
        Pair(R.id.bottom_view_earth, EarthFragment()),
        Pair(R.id.bottom_view_mars, MarsFragment()),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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