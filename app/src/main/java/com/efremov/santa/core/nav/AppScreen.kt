package com.efremov.santa.core.nav

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen

abstract class AppScreen : Screen() {

    open fun getFragment(): Fragment? {
        return null
    }
    
    open fun getActivityIntent(context: Context): Intent? {
        return null
    }
}