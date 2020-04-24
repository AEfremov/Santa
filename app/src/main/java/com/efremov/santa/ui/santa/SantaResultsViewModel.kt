package com.efremov.santa.ui.santa

import androidx.lifecycle.MutableLiveData
import com.efremov.santa.App
import com.efremov.santa.core.common.BaseViewModel
import com.efremov.santa.data.UserDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SantaResultsViewModel : BaseViewModel() {

    var santaResultObserver = MutableLiveData<UserDto>()

    override fun initModel() {
    }

    init {
    }

    fun showSantaResult(groupId: String) {
        showProgress.value = true
        GlobalScope.launch(Dispatchers.Main) {
            val request = App.repository.showSanta(groupId, App.prefs.userId)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    showProgress.value = false
                    if (response.body() != null) {
                        santaResultObserver.value = response.body()!!.user
                    }
                } else {
                    onError(response.errorBody()!!.string())
                }
            } catch (e: HttpException) {
                e.printStackTrace()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}