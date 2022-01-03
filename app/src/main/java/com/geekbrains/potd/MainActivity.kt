package com.geekbrains.potd

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.geekbrains.potd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        mainViewModel.themeId?.let { setTheme(it) }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.topToolbar.setOnMenuItemClickListener { onOptionsItemSelected(it) }
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