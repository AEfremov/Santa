package com.efremov.santa.ui.group

import androidx.lifecycle.MutableLiveData
import com.efremov.santa.App
import com.efremov.santa.core.common.BaseViewModel
import com.efremov.santa.core.widget.AppScreens
import com.efremov.santa.data.local.SantaPref
import com.efremov.santa.data.CreateGroupParams
import com.efremov.santa.data.GroupDto
import com.efremov.santa.data.JoinGroupParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class GroupsViewModel : BaseViewModel() {

    var createGroupObserver = MutableLiveData<GroupDto>()
    var joinGroupObserver = MutableLiveData<GroupDto>()
    var groupsData = MutableLiveData<List<GroupDto>>()

    init {
        createGroupObserver.value = null
        joinGroupObserver.value = null
        groupsData.value = null
    }

    override fun initModel() {
    }

    fun joinGroup(code: String) {
        showProgress.value = true
        GlobalScope.launch(Dispatchers.Main) {
            val params = JoinGroupParams(code, SantaPref.userId)
            val request = App.repository.joinGroup(params)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    showProgress.value = false
                    if (response.body()!!.group != null) {
                        joinGroupObserver.value = response.body()!!.group
                    }
                } else {
                    onError(response.errorBody()!!.string())
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    fun createGroup(name: String) {
        showProgress.value = true
        GlobalScope.launch(Dispatchers.Main) {
            val params = CreateGroupParams(name, SantaPref.userId)
            val request = App.repository.createGroup(params)
            try {
                val response = request.await()
                if (response.isSuccessful) {
                    showProgress.value = false
                    if (response.body() != null) {
                        if (response.body()!!.group != null) {
                            createGroupObserver.value = response.body()!!.group
                        }
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

    fun obtainGroups(needShowProgressDialog: Boolean) {
        if (needShowProgressDialog) {
            showProgress.value = true
        }

        GlobalScope.launch(Dispatchers.Main) {
            val request = App.repository.obtainGroups(SantaPref.userId/*"4525435"*/)
            try {
                val response = request.await()
                if (response.isSuccessful) {

                    if (needShowProgressDialog) {
                        showProgress.value = false
                    }

                    if (response.body() != null) {
                        if (response.body()!!.groups != null) {
                            groupsData.value = response.body()!!.groups
                        }
                    }
                } else {
                    onError(response.errorBody()!!.string())
                    groupsData.value = mutableListOf()
                }
            } catch (e: HttpException) {
                e.printStackTrace()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
//            if (response.isSuccessful) {
//                showProgress.value = false
//                if (response.body() != null) {
//                    if (response.body()!!.groups != null) {
//                        groupsData.value = response.body()!!.groups
//                    }
//                }
//            } else {
//                showProgress.value = false
////                groupsData.value = mutableListOf()
//            }
        }
    }

    fun toSantaScreen(groupId: String, groupName: String) {
        App.router.navigateTo(
            AppScreens.SantaScreen(
                groupId,
                groupName)
        )
    }
}