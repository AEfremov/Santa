package com.efremov.santa.core.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder

abstract class BaseViewModel : ViewModel() {

    var showProgress = MutableLiveData<Boolean>()
    var errorThrowable = SingleLiveEvent<Throwable>()
    var errorBodyObserver = SingleLiveEvent<String>()

    protected abstract fun initModel()

    protected fun onError(throwable: Throwable) {
        showProgress.value = false
        errorThrowable.value = throwable
    }

    protected fun onError(errorBody: String) {
        showProgress.value = false

        val errors: JSONObject = JSONObject(errorBody)
        val values: JSONArray = errors.optJSONArray("errors")
        val sb: StringBuilder = StringBuilder()
        for (i in 0 until values.length()) {
            sb.append(values.optString(i)).append("\n")
        }
        errorBodyObserver.value = sb.toString()
    }
}