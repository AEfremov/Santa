package com.efremov.santa.ui.registration

import androidx.lifecycle.MutableLiveData
import com.efremov.santa.App
import com.efremov.santa.core.widget.AppScreens
import com.efremov.santa.data.local.SantaPref
import com.efremov.santa.core.common.BaseViewModel
import com.efremov.santa.data.SignUpParams
import kotlinx.coroutines.*
import retrofit2.HttpException

class RegistrationViewModel : BaseViewModel() {

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val agreement = MutableLiveData<Boolean>()
    val signUpParams = MutableLiveData<SignUpParams>()
    val invalidParamsObserver = MutableLiveData<Boolean>()
    val registrationObserver = MutableLiveData<Boolean>()

    init {
        firstName.value = ""
        lastName.value = ""
        agreement.value = true
        signUpParams.value = SignUpParams()
    }

    override fun initModel() {
    }

    @ExperimentalCoroutinesApi
    fun signUp() {

//        if (firstName.value!!.isEmpty() || lastName.value!!.isEmpty()) {
//            invalidParamsObserver.value = false
//        } else {
        showProgress.value = true
            GlobalScope.launch(Dispatchers.Main) {
                val signUpParams = SignUpParams(
                    first_name = firstName.value!!,
                    last_name = lastName.value!!
                )
                val request = App.repository.signUp(signUpParams)
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        showProgress.value = false
                        SantaPref.firstName = response.body()!!.user.first_name
                        SantaPref.lastName = response.body()!!.user.last_name
                        SantaPref.userId = response.body()!!.user.id
                        registrationObserver.value = true
                    } else {
                        onError(response.errorBody()!!.string())
                        registrationObserver.value = false
                    }
                } catch (e: HttpException) {
                    e.printStackTrace()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
//        }
    }

    fun toGroupsScreen() {
        App.router.newRootScreen(AppScreens.GroupsScreen())
    }
}