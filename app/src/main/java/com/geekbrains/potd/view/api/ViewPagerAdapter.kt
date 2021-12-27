package com.geekbrains.potd.view.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.geekbrains.potd.R
import com.geekbrains.potd.view.api.earth.EarthFragment
import com.geekbrains.potd.view.api.mars.MarsFragment
import com.geekbrains.potd.view.api.system.SystemFragment

class ViewPagerAdapter(private val fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    class FragmentInfo(
        val fragment: Fragment,
        val title: String,
        val iconId: Int,
        val layoutId: Int,
    )

    private val info = arrayOf(
        FragmentInfo(
            SystemFragment(),
            "System",
            R.drawable.ic_tabitem_system,
            R.layout.activity_api_tabitem_system
        ),
        FragmentInfo(
            EarthFragment(),
            "Earth",
            R.drawable.ic_tabitem_earth,
            R.layout.activity_api_tabitem_earth
        ),
        FragmentInfo(
            MarsFragment(),
            "Mars",
            R.drawable.ic_tabitem_mars,
            R.layout.activity_api_tabitem_mars
        ),
    )

    override fun getCount(): Int = info.size

    override fun getItem(position: Int): Fragment = info[position].fragment

    override fun getPageTitle(position: Int): CharSequence = info[position].title

    private fun at(pos: Int): FragmentInfo? = if (0 <= pos && pos < info.size) info[pos] else null

    fun getIconId(position: Int): Int? = at(position)?.iconId

    fun getLayoutId(position: Int): Int? = at(position)?.layoutId
}