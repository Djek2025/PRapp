package com.example.prapp.utils

import android.content.SharedPreferences

class SPCache(private val sp: SharedPreferences) {

    companion object{

        internal const val ARG_FIRST_LAUNCH = "first_launch"
        internal const val ARG_FIRST_LAUNCH_DEF = true

        internal const val ARG_LAST_PAGE = "last_page"
        internal const val ARG_LAST_PAGE_DEF = "https://play.google.com"

    }

    var isFirstLaunch
        get() = sp.getBoolean(ARG_FIRST_LAUNCH, ARG_FIRST_LAUNCH_DEF)
        set(value) = sp.edit().putBoolean(ARG_FIRST_LAUNCH, value).apply()

    var lastPage
        get() = sp.getString(ARG_LAST_PAGE, ARG_LAST_PAGE_DEF)
        set(value) = sp.edit().putString(ARG_LAST_PAGE, value).apply()

}