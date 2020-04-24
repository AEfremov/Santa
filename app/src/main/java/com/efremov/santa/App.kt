package com.efremov.santa

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.efremov.santa.core.utils.FontManager
import com.efremov.santa.data.server.Repository
import com.efremov.santa.data.local.SantaPref
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class App : Application() {
    
    companion object {
        private val cicerone: Cicerone<Router> = Cicerone.create()
        val navigatorHolder: NavigatorHolder = cicerone.navigatorHolder
        val router: Router = cicerone.router
        val repository: Repository = Repository()
        val prefs = SantaPref
        const val apkUrl = "https://secretsantaltech.page.link/nM9x"
    }

    override fun onCreate() {
        super.onCreate()

        val assetManager = assets
        FontManager.init(assetManager)

        Kotpref.init(this)
    }
}