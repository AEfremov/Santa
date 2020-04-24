package com.efremov.santa.ui.santa

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.efremov.santa.App
import com.efremov.santa.core.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_santa_results.*

class SantaResultsFragment : BaseFragment() {

    override val layoutRes: Int = com.efremov.santa.R.layout.fragment_santa_results

//    private lateinit var viewModel: SantaResultsViewModel
    private val viewModel: SantaResultsViewModel by lazy { ViewModelProviders.of(this@SantaResultsFragment)
        .get(SantaResultsViewModel::class.java) }

    private var groupId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        groupId = arguments?.getSerializable("GROUP_ID") as String
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeView()

//        viewModel = ViewModelProviders.of(activity!!).get(SantaResultsViewModel::class.java)
        viewModel.showSantaResult(groupId)

        toolbar.setNavigationOnClickListener { App.router.exit() }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.santaResultObserver.value = null
        viewModel.santaResultObserver.removeObservers(this@SantaResultsFragment)
        viewModel.showProgress.removeObservers(this@SantaResultsFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun observeView() {
        viewModel.santaResultObserver.observe(this@SantaResultsFragment, Observer {
            val fullName = "${it.first_name} ${it.last_name}"
            name_view.text = fullName
        })

        viewModel.showProgress.observe(this@SantaResultsFragment, Observer { // Boolean
            if (it == true) showProgress() else hideProgress()
        })

        viewModel.errorBodyObserver.observe(this@SantaResultsFragment, Observer {
            onError(it)
        })
    }

    companion object {

        fun getInstance(groupId: String) : SantaResultsFragment {
            val fragment = SantaResultsFragment()
            val args = Bundle()
            args.putSerializable("GROUP_ID", groupId)
            fragment.arguments = args

            return fragment
        }
    }
}