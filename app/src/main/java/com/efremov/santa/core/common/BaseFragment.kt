package com.efremov.santa.core.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.efremov.santa.ext.onError

abstract class BaseFragment :
    Fragment(),
    BaseView
{

    companion object {
        private val PROGRESS_TAG = "bf_progress"
    }

    abstract val layoutRes: Int

    abstract fun observeView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        inflater.inflate(layoutRes, container, false)

    protected fun showProgressDialog(progress: Boolean) {
        if (!isAdded) return

        val fragment = childFragmentManager.findFragmentByTag(PROGRESS_TAG)
        if (fragment != null && !progress) {
            (fragment as ProgressDialog).dismissAllowingStateLoss()
            childFragmentManager.executePendingTransactions()
        } else if (fragment == null && progress) {
            ProgressDialog()
                .show(childFragmentManager, PROGRESS_TAG)
            childFragmentManager.executePendingTransactions()
        }
    }

    override fun onMessage(title: String, message: String) {
    }

    override fun onMessage(message: String) {
    }

    override fun onError(errorTitle: String, errorMessage: String) {
        context.onError(errorTitle, errorMessage)
    }

    override fun onError(error: String) {
        context.onError(error)
    }

    override fun onError(throwable: Throwable) {
    }

    override fun onError(messageResId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        showProgressDialog(true)
    }

    override fun hideProgress() {
        showProgressDialog(false)
    }
}