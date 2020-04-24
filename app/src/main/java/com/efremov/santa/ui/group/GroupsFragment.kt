package com.efremov.santa.ui.group

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efremov.santa.R
import com.efremov.santa.data.local.SantaPref
import com.efremov.santa.core.common.BaseFragment
import com.efremov.santa.core.common.BaseRecyclerViewAdapter
import com.efremov.santa.core.utils.ConnectivityHelper
import com.efremov.santa.core.widget.ConfirmDialog
import com.efremov.santa.data.GroupDto
import com.efremov.santa.ext.visible
import kotlinx.android.synthetic.main.fragment_groups.*

class GroupsFragment :
    BaseFragment(),
    GroupSelectorDialog.GroupSelectorListener,
    ConfirmDialog.OnClickListener
{

    override val layoutRes: Int = com.efremov.santa.R.layout.fragment_groups

    val GROUP_COMPLETE_TAG = "GROUP_COMPLETE_TAG"

//    private lateinit var viewModel: GroupsViewModel
    private val viewModel: GroupsViewModel by lazy { ViewModelProviders.of(this@GroupsFragment)
        .get(GroupsViewModel::class.java) }

    private lateinit var adapter: BaseRecyclerViewAdapter<GroupDto>

    private var lastClickTime: Long = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeView()

//        viewModel = ViewModelProviders.of(activity!!).get(GroupsViewModel::class.java)

        if (ConnectivityHelper.isConnectedToInternet(context!!)) {
            retry_load_data.visible(false)
            viewModel.obtainGroups(true)
        } else {
            Toast.makeText(context, "Проверьте интернет-соединение!", Toast.LENGTH_SHORT).show()
            retry_load_data.visible(true)
        }

        group_fab_view.setOnClickListener {
            GroupSelectorDialog(activity!!, this).show()
        }

        swipeContainer.setOnRefreshListener {
            viewModel.obtainGroups(false)
        }

        retry_load_data.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) return@setOnClickListener
            lastClickTime = SystemClock.elapsedRealtime()

            if (ConnectivityHelper.isConnectedToInternet(context!!)) {
                retry_load_data.visible(false)
                viewModel.obtainGroups(true)
            } else {
                Toast.makeText(context, "Проверьте интернет-соединение!", Toast.LENGTH_SHORT).show()
                retry_load_data.visible(true)
            }
        }
    }

    override fun observeView() {
        viewModel.joinGroupObserver.observe(this@GroupsFragment, Observer { data ->
            if (data != null) {
                ConfirmDialog
                    .newInstance(
                        title = activity!!.getString(R.string.groups_screen_welcome),
                        msg = "в группу ${data.name}",
                        positive = activity!!.getString(R.string.action_ok),
                        negative = activity!!.getString(R.string.empty),
                        tag = GROUP_COMPLETE_TAG
                    )
                    .show(childFragmentManager, GROUP_COMPLETE_TAG)
            }
        })

        viewModel.createGroupObserver.observe(this@GroupsFragment, Observer { data ->
            if (data != null) {
                ConfirmDialog
                    .newInstance(
                        title = activity!!.getString(R.string.groups_screen_welcome),
                        msg = "в группу ${data.name}",
                        positive = activity!!.getString(R.string.action_ok),
                        negative = activity!!.getString(R.string.empty),
                        tag = GROUP_COMPLETE_TAG
                    )
                    .show(childFragmentManager, GROUP_COMPLETE_TAG)
            }
        })

        viewModel.groupsData.observe(this@GroupsFragment, Observer { groups ->
            if (groups != null) {
                if (groups.isEmpty()) {
                    balls_view.visible(true)
                    person_name_view.visible(true)
                    groups_search_view.visible(true)
                    swipeContainer.visible(false)
                    groups_recycler_view.visible(false)

                    val personName = "${SantaPref.firstName} ${SantaPref.lastName}"
                    person_name_view.text = personName
                } else {
                    balls_view.visible(false)
                    person_name_view.visible(false)
                    groups_search_view.visible(false)
                    swipeContainer.visible(true)
                    groups_recycler_view.visible(true)

                    adapter = BaseRecyclerViewAdapter(
                        layout = R.layout.item_group,
                        items = mutableListOf(),
                        holderFactory = { GroupItemHolder(it) },
                        onItemClick = { item, _ ->
                            viewModel.toSantaScreen(item.id, item.name)
                        }
                    )
                    groups_recycler_view.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager
                    groups_recycler_view.adapter = adapter
                    adapter.clear()
                    adapter.addItems(groups)
                }

                group_fab_view.visible(true)
                swipeContainer.isRefreshing = false
            }
        })

        viewModel.showProgress.observe(this@GroupsFragment, Observer { // Boolean
            if (it == true) showProgress() else hideProgress()
        })

        viewModel.errorBodyObserver.observe(this@GroupsFragment, Observer {
            onError(it)
        })
    }

    override fun connectToGroupByCode(code: String) {
        viewModel.joinGroup(code)
    }

    override fun createGroupByName(name: String) {
        viewModel.createGroup(name)
    }

    override fun dialogConfirm(tag: String) {
        when (tag) {
            GROUP_COMPLETE_TAG -> {
                viewModel.obtainGroups(true)
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.createGroupObserver.value = null
        viewModel.createGroupObserver.removeObservers(this@GroupsFragment)
        viewModel.joinGroupObserver.value = null
        viewModel.joinGroupObserver.removeObservers(this@GroupsFragment)
        viewModel.groupsData.removeObservers(this@GroupsFragment)
    }

    companion object {

        fun getInstance() : GroupsFragment {
            val fragment = GroupsFragment()

            return fragment
        }
    }
}
