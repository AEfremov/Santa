package com.efremov.santa.ui.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.efremov.santa.R
import com.efremov.santa.core.nav.AppNavigator
import com.efremov.santa.App
import ru.terrakok.cicerone.commands.Command

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private val navigator = object : AppNavigator(this, R.id.container) {
        override fun applyCommands(commands: Array<Command>) {
            super.applyCommands(commands)
            supportFragmentManager.executePendingTransactions()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        if (App.prefs.userId.isNotEmpty()) {
            Log.d("USER_ID", App.prefs.userId)
            viewModel.toGroupsScreen()
        } else {
            viewModel.toRegistrationScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        App.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        App.navigatorHolder.removeNavigator()
    }

    private fun getCurrentFragment() : Fragment? {
        return supportFragmentManager.findFragmentById(R.id.container)
    }

    companion object {

        fun start(activity: Activity): Intent {
            val intent = Intent(activity, MainActivity::class.java)
            activity.overridePendingTransition(0, 0)
            return intent
        }
    }
}
