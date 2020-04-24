package com.efremov.santa.core.common

interface BaseView {
    fun onMessage(title: String, message: String)
    fun onMessage(message: String)
    fun onError(errorTitle: String, errorMessage: String)
    fun onError(error: String)
    fun onError(throwable: Throwable)
    fun onError(messageResId: Int)
    fun showProgress()
    fun hideProgress()
}