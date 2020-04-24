package com.efremov.santa.ui.santa

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.efremov.santa.R
import com.efremov.santa.App
import com.efremov.santa.core.common.BaseFragment
import com.efremov.santa.core.common.BaseRecyclerViewAdapter
import com.efremov.santa.core.utils.ConnectivityHelper
import com.efremov.santa.data.GroupUserDto
import com.efremov.santa.ext.shareContent
import kotlinx.android.synthetic.main.fragment_santa.*

class SantaFragment : BaseFragment() {

    override val layoutRes: Int = com.efremov.santa.R.layout.fragment_santa

//    private lateinit var viewModel: SantaViewModel
    private val viewModel: SantaViewModel by lazy { ViewModelProviders.of(this@SantaFragment).get(
    SantaViewModel::class.java) }

    val SANTA_TYPE_DRAW_SANTA = "SANTA_TYPE_START_MIRACLE"
    val SANTA_TYPE_WATCH_RESULTS = "SANTA_TYPE_WATCH_RESULTS"
    var selectedType: String = ""

    data class SantaScreenData(var groupId: String = "",
                               var groupName: String = "",
                               var inviteCode: String = "",
                               var isSantaDraw: Boolean = false)

    private var santaScreenData: SantaScreenData

//    private var groupId: String = ""
//    private var groupName: String = ""
//    private var inviteCode: String = ""

    private lateinit var adapter: BaseRecyclerViewAdapter<GroupUserDto>

    private var lastClickTime: Long = 0

    init {
        santaScreenData = SantaScreenData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        santaScreenData.groupId = arguments?.getSerializable("GROUP_ID") as String
        santaScreenData.groupName = arguments?.getSerializable("GROUP_NAME") as String
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeView()

//        viewModel = ViewModelProviders.of(activity!!).get(SantaViewModel::class.java)

        toolbarTitle.text = santaScreenData.groupName
        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
                R.id.addPersonAction -> {
//                    shareContent("Введи: ${santaScreenData.inviteCode}. Тайный Санта ждет тебя: ${App.apkUrl}")
                    shareContent("Введи: ${santaScreenData.inviteCode}. Тайный Санта ждет тебя: ${App.apkUrl}")
                }
            }
            true
        }
        toolbar.inflateMenu(R.menu.menu_santa)
        toolbar.setNavigationOnClickListener { App.router.exit() }

        if (ConnectivityHelper.isConnectedToInternet(context!!)) {
            if (santaScreenData.groupId.isNotEmpty()) {
                viewModel.obtainGroupData(santaScreenData.groupId, true)
            }
        } else {
            Toast.makeText(context, "Проверьте интернет-соединение!", Toast.LENGTH_SHORT).show()
        }

        action_santa.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) return@setOnClickListener
            lastClickTime = SystemClock.elapsedRealtime()

            when (selectedType) {
                SANTA_TYPE_DRAW_SANTA -> {
                    viewModel.drawSanta(santaScreenData.groupId)
                }
                SANTA_TYPE_WATCH_RESULTS -> {
                    viewModel.toSantaResultsFragment(santaScreenData.groupId)
                }
            }
        }

        swipeContainer.setOnRefreshListener {
            if (ConnectivityHelper.isConnectedToInternet(context!!)) {
                if (santaScreenData.groupId.isNotEmpty()) {
                    viewModel.obtainGroupData(santaScreenData.groupId, false)
                }
            } else {
                Toast.makeText(context, "Проверьте интернет-соединение!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun observeView() {
        viewModel.groupDataObserver.observe(this@SantaFragment, Observer { data ->
            if (data != null) {
                santaScreenData.isSantaDraw = data.is_draw
                santaScreenData.inviteCode = data.code

                adapter = BaseRecyclerViewAdapter(
                    layout = R.layout.item_person,
                    items = mutableListOf(),
                    holderFactory = { PersonViewHolder(it) })
//                swipeContainer.visible(true)
//                groups_recycler_view.visible(true)
                groups_recycler_view.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager
                groups_recycler_view.adapter = adapter
                adapter.addItems(data.users)

                if (data.is_draw && data.owner) {
                    action_santa.isEnabled = true
                    selectedType = SANTA_TYPE_WATCH_RESULTS
                    action_santa.text = activity!!.getString(R.string.santa_screen_show_result)
                    toolbar.menu.findItem(R.id.addPersonAction).isVisible = false
                } else if (!data.is_draw && data.owner) {
                    action_santa.isEnabled = true
                    selectedType = SANTA_TYPE_DRAW_SANTA
                    action_santa.text = activity!!.getString(R.string.groups_screen_start_santa)
                    toolbar.menu.findItem(R.id.addPersonAction).isVisible = true
//                    toolbar.inflateMenu(R.menu.menu_santa)
                } else if (data.is_draw) {
                    action_santa.isEnabled = true
                    selectedType = SANTA_TYPE_WATCH_RESULTS
                    action_santa.text = activity!!.getString(R.string.santa_screen_show_result)
                    toolbar.menu.findItem(R.id.addPersonAction).isVisible = false
                } else {
//                    toolbar.inflateMenu(R.menu.menu_santa)
                    action_santa.isEnabled = false
                    action_santa.text = "Санта еще не разыгран"
                    toolbar.menu.findItem(R.id.addPersonAction).isVisible = true
                }
            }

            swipeContainer.isRefreshing = false
        })

        viewModel.drawSantaObserver.observe(this@SantaFragment, Observer { data ->
            if (data != null) {
                MiracleDialogFragment().show(childFragmentManager, MiracleDialogFragment::class.java.name)
                selectedType = SANTA_TYPE_WATCH_RESULTS
                action_santa.text = activity!!.getString(R.string.santa_screen_show_result)
                toolbar.menu.findItem(R.id.addPersonAction).isVisible = false
            }
        })

        viewModel.showProgress.observe(this@SantaFragment, Observer { // Boolean
            if (it == true) showProgress() else hideProgress()
        })

        viewModel.errorBodyObserver.observe(this@SantaFragment, Observer {
            onError(it)
        })
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.groupDataObserver.value = null
        viewModel.groupDataObserver.removeObservers(this@SantaFragment)
        viewModel.drawSantaObserver.value = null
        viewModel.drawSantaObserver.removeObservers(this@SantaFragment)
        viewModel.showProgress.removeObservers(this@SantaFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        
        fun getInstance(groupId: String, groupName: String) : SantaFragment {
            val fragment = SantaFragment()
//            fragment.setExtArguments(groupId, "GROUP_ID")
            val args = Bundle()
            args.putSerializable("GROUP_ID", groupId)
            args.putSerializable("GROUP_NAME", groupName)
            fragment.arguments = args
            return fragment
        }
    }
}
