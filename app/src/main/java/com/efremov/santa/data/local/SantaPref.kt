package com.efremov.santa.data.local

import com.chibatching.kotpref.KotprefModel

object SantaPref : KotprefModel() {

    var firstName by stringPref(default = "", key = null, commitByDefault = false)
    var lastName by stringPref(default = "", key = null, commitByDefault = false)
    var userId by stringPref(default = "", key = null, commitByDefault = false)
    var haveGroup by booleanPref(default = false, key = null, commitByDefault = false)
}