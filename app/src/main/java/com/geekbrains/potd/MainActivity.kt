package com.geekbrains.potd

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.geekbrains.potd.databinding.ActivityMainBinding
import com.geekbrains.potd.demo.CollapsingToolbarFragment
import com.geekbrains.potd.demo.MotionFragment

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

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
            R.id.menuThemeSteel -> setThemeRecreate(R.style.Theme_Base_BlueGray)
            R.id.menuThemeCopper -> setThemeRecreate(R.style.Theme_Base_OrangeGreen)
            R.id.menuCollapsingToolbar -> pushFragment(CollapsingToolbarFragment())
            R.id.menuMotionFragment -> pushFragment(MotionFragment())
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setThemeRecreate(themeId: Int): Boolean {
        mainViewModel.themeId = themeId
        recreate()
        return true
    }

    private fun pushFragment(fragment: Fragment): Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .addToBackStack(null)
            .commit()
        return true
    }
}