package com.vlg.list

import androidx.fragment.app.Fragment

interface Navigator {
    fun startFragment(fragment: Fragment)

    class Empty : Navigator {
        override fun startFragment(fragment: Fragment) {}
    }
}