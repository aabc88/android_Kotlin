package com.example.ch02.section02

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragment
import com.example.ch02.R

class Test2Activity : AppCompatActivity(), PreferenceFragment.OnPreferenceStartFragmentCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_test2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportFragmentManager.beginTransaction().replace(R.id.main, MainSettingFragment()).commit()
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragment,
        pref: Preference
    ): Boolean {
        val arg = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(
            classLoader, pref.fragment as String
        )
        fragment.arguments = arg
        supportFragmentManager.beginTransaction().replace(R.id.main, fragment).addToBackStack(null).commit()
        return true
    }
}