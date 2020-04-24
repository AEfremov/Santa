package com.efremov.santa.ui.santa

import androidx.lifecycle.MutableLiveData
import com.efremov.santa.R
import com.efremov.santa.App
import com.efremov.santa.core.common.BaseViewModel
import com.efremov.santa.core.widget.AppScreens
import com.efremov.santa.data.AvatarObj
import com.efremov.santa.data.local.SantaPref
import com.efremov.santa.data.GroupDto
import com.efremov.santa.data.SantaDrawParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.*

class SantaViewModel : BaseViewModel() {

    val groupDataObserver = MutableLiveData<GroupDto>()
    val drawSantaObserver = MutableLiveData<GroupDto>()
    val avatars = MutableLiveData<List<AvatarObj>>()

    init {
        groupDataObserver.value = null
        drawSantaObserver.value = null

        val avGreen = AvatarObj(R.drawable.avatar_green, 1)
        val avRed = AvatarObj(R.drawable.avatar_red, 2)
        val avTurquoise = AvatarObj(R.drawable.avatar_turquoise, 3)
        val avYellow = AvatarObj(R.drawable.avatar_yellow, 4)
        avatars.value = listOf(avGreen, avRed, avTurquoise, avYellow)
    }

    override fun initModel() {
    }

    fun obtainGroupData(groupId: String, needShowProgressDialog: Boolean) {
        if (needShowProgressDialog) {
            showProgress.value = true
        }

        GlobalScope.launch(Dispatchers.Main) {
            val request = App.repository.getGroupById(SantaPref.userId, groupId)
            try {
                val response = request.await()
                if (response.isSuccessful) {

                    if (needShowProgressDialog) {
                        showProgress.value = false
                    }

                    val data = response.body()!!.group

                    if (data != null) {
                        data.users[0].avatar = avatars.value!![Random().nextInt(avatars.value!!.size)]
                        for (i in 1 until data.users.size) {
                            do {
                                val index = Random().nextInt(avatars.value!!.size)
                                data.users[i].avatar = avatars.value!![index]
                            } while (data.users[i].avatar.avatarId == data.users[i - 1].avatar.avatarId)
                        }

//                        groupDataObserver.value = response.body()!!.group
                        groupDataObserver.value = data
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

    fun drawSanta(groupId: String) {
        showProgress.value = true
        GlobalScope.launch(Dispatchers.Main) {
            val params = SantaDrawParams(App.prefs.userId, groupId)
            try {
                val request = App.repository.drawSanta(params)
                val response = request.await()
                if (response.isSuccessful) {
                    showProgress.value = false
                    drawSantaObserver.value = response.body()!!.group
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

    fun toSantaResultsFragment(groupId: String) {
        App.router.navigateTo(AppScreens.SantaResultsScreen(groupId))
    }
}