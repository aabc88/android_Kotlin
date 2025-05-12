package com.example.ch02.section02

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.ch02.R

class BSettingFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.setting_b, rootKey)
    }
}