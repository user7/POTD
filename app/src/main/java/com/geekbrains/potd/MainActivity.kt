package com.geekbrains.potd

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.geekbrains.potd.databinding.ActivityMainBinding
import com.geekbrains.potd.fragments.earth.EarthFragment
import com.geekbrains.potd.fragments.mars.MarsFragment
import com.geekbrains.potd.fragments.system.SystemFragment

class MainActivity : AppCompatActivity() {
    val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding
    private val navFragments = mapOf(
        Pair(R.id.bottom_view_system, SystemFragment()),
        Pair(R.id.bottom_view_earth, EarthFragment()),
        Pair(R.id.bottom_view_mars, MarsFragment()),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        mainViewModel.themeId?.let { setTheme(it) }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnItemSelectedListener {
            navFragments[it.itemId]?.let { fragment ->
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
                true
            } ?: false
        }
        binding.topToolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
        binding.bottomNavigationView.selectedItemId = R.id.bottom_view_system
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.themeSteel -> setThemeRecreate(R.style.Theme_Steel)
            R.id.themeCopper -> setThemeRecreate(R.style.Theme_Copper)
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setThemeRecreate(themeId: Int): Boolean {
        mainViewModel.themeId = themeId
        recreate()
        return true
    }
}