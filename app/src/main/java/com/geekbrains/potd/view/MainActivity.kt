package com.geekbrains.potd.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geekbrains.potd.R
import com.geekbrains.potd.view.picture.POTDFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                POTDFragment.newInstance()
            ).commit()
        }
    }
}