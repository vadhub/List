package com.vlg.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vlg.list.ui.GroupFragment

class MainActivity : AppCompatActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startFragment(GroupFragment())
    }

    override fun startFragment(fragment: Fragment) {
       supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }


}