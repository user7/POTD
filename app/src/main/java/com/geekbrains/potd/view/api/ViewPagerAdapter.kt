package com.geekbrains.potd.view.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.geekbrains.potd.R

class ViewPagerAdapter(private val fm : FragmentManager) : FragmentStatePagerAdapter(fm) {
    class FragmentInfo(val fragment: Fragment, val title: String, val iconId: Int)

    private val info = arrayOf(
        FragmentInfo(SystemFragment(), "System", R.drawable.bg_system),
        FragmentInfo(EarthFragment(), "Earth", R.drawable.bg_earth),
        FragmentInfo(MarsFragment(), "Mars", R.drawable.bg_mars),
    )

    override fun getCount(): Int = info.size

    override fun getItem(position: Int): Fragment = info[position].fragment

    override fun getPageTitle(position: Int): CharSequence = info[position].title

    fun getIconId(position: Int): Int = info[position].iconId
}