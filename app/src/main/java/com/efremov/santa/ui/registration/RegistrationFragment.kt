package com.efremov.santa.ui.registration

import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.efremov.santa.R
import com.efremov.santa.core.common.BaseFragment
import com.efremov.santa.ext.tryOpenLink
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class RegistrationFragment : BaseFragment() {
    
    override val layoutRes: Int = com.efremov.santa.R.layout.fragment_registration

    private val viewModel: RegistrationViewModel by lazy { ViewModelProviders.of(this@RegistrationFragment)
        .get(RegistrationViewModel::class.java) }

    private var lastClickTime: Long = 0

    @ExperimentalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeView()

//        viewModel = ViewModelProviders.of(activity!!).get(RegistrationViewModel::class.java)

        first_name_view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.firstName.value = s.toString()
                enableConfirmView()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        last_name_view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.lastName.value = s.toString()
                enableConfirmView()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        confirm_view.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) return@setOnClickListener
            lastClickTime = SystemClock.elapsedRealtime()

            viewModel.firstName.value = first_name_view.text.toString()
            viewModel.lastName.value = last_name_view.text.toString()
            viewModel.signUp()
        }

        agreement_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.agreement.value = isChecked
            enableConfirmView()
        }

        agreement_label.setOnClickListener {
            context?.tryOpenLink(getString(R.string.secret_santa_confidence_url))
        }
    }

    override fun observeView() {
        viewModel.invalidParamsObserver.observe(this@RegistrationFragment, Observer {
            if (!it) {
                Toast.makeText(context, activity!!.getString(R.string.reg_screen_name_not_empty), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.registrationObserver.observe(this@RegistrationFragment, Observer { signUpCompleted ->
            if (signUpCompleted) {
                viewModel.toGroupsScreen()
            } else {
                first_name_view.setText("")
                viewModel.firstName.value = ""
                last_name_view.setText("")
                viewModel.lastName.value = ""
            }
        })

        viewModel.showProgress.observe(this@RegistrationFragment, Observer { // Boolean
            if (it == true) showProgress() else hideProgress()
        })

        viewModel.errorBodyObserver.observe(this@RegistrationFragment, Observer {
            onError(it)
        })
    }

    private fun enableConfirmView() {
        confirm_view.isEnabled = viewModel.firstName.value!!.isNotEmpty() &&
                viewModel.lastName.value!!.isNotEmpty() &&
                viewModel.agreement.value!!
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.invalidParamsObserver.removeObservers(this@RegistrationFragment)
        viewModel.registrationObserver.removeObservers(this@RegistrationFragment)
        viewModel.showProgress.removeObservers(this@RegistrationFragment)
    }

    companion object {
        fun getInstance() : RegistrationFragment {
            val fragment = RegistrationFragment()
            
            return fragment
        }
    }
}