package com.efremov.santa.core.widget

import androidx.fragment.app.Fragment
import com.efremov.santa.core.nav.AppScreen
import com.efremov.santa.ui.group.GroupsFragment
import com.efremov.santa.ui.registration.RegistrationFragment
import com.efremov.santa.ui.santa.SantaFragment
import com.efremov.santa.ui.santa.SantaResultsFragment

object AppScreens {

    class RegistrationScreen : AppScreen() {
        override fun getFragment(): Fragment {
            return RegistrationFragment.getInstance()
        }
    }

    class GroupsScreen : AppScreen() {
        override fun getFragment(): Fragment? {
            return GroupsFragment.getInstance()
        }
    }

    class SantaScreen(private val groupId: String, private val groupName: String) : AppScreen() {
        override fun getFragment(): Fragment? {
            return SantaFragment.getInstance(groupId, groupName)
        }
    }

    class SantaResultsScreen(private val groupId: String) : AppScreen() {
        override fun getFragment(): Fragment? {
            return SantaResultsFragment.getInstance(groupId)
        }
    }
}