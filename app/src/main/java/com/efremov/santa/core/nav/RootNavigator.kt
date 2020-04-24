package com.efremov.santa.core.nav

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.efremov.santa.ui.group.GroupsFragment
import com.efremov.santa.ui.registration.RegistrationFragment
import com.efremov.santa.common.Screens
import ru.terrakok.cicerone.commands.Command

class RootNavigator(
        activity: FragmentActivity,
        containerId: Int
) : AppNavigator(activity, containerId) {
    
    override fun createStartActivityOptions(command: Command?, activityIntent: Intent?): Bundle {
        return super.createStartActivityOptions(command, activityIntent)
    }
    
    override fun createFragment(screen: AppScreen): Fragment {
        return when(screen.screenKey) {
            Screens.REGISTRATION -> RegistrationFragment.getInstance()
            Screens.GROUPS -> GroupsFragment.getInstance()
//            Screens.SANTA           -> SantaFragment.getInstance()
            else -> throw IllegalArgumentException()
        }
    }
}