package com.example.aven.projekt2

import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager

/**
 * Created by Aven on 2018-05-27.
 */
class SettingsFragment: PreferenceFragment() {
    val PREF_NAME = "APP_SETTINGS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager.sharedPreferencesName=PREF_NAME

        addPreferencesFromResource(R.xml.preference_xml)
    }

    override fun onPause() {

        super.onPause()
    }
}