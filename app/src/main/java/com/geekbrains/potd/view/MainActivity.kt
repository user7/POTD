package com.geekbrains.potd.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.geekbrains.potd.R
import com.geekbrains.potd.view.picture.POTDFragment
import com.geekbrains.potd.viewmodel.POTDViewModel

class MainActivity : AppCompatActivity(), ChangeThemeCallback {
    val viewModel: POTDViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(viewModel.themeId ?: R.style.Theme_Steel)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                POTDFragment.newInstance()
            ).commit()
        }
    }

    override fun onChangeTheme(themeId : Int) {
        viewModel.themeId = themeId
        recreate()
    }
}