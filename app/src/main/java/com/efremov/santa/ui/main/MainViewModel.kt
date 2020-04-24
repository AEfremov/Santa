package com.efremov.santa.ui.main

import androidx.lifecycle.ViewModel
import com.efremov.santa.App
import com.efremov.santa.core.widget.AppScreens

class MainViewModel : ViewModel() {

    fun toRegistrationScreen() {
        App.router.newRootScreen(AppScreens.RegistrationScreen())
    }

    fun toGroupsScreen() {
        App.router.newRootScreen(AppScreens.GroupsScreen())
    }
}