package com.efremov.santa.ui.group

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import com.efremov.santa.R
import com.efremov.santa.ext.visible
import kotlinx.android.synthetic.main.dialog_group_selector.*

class GroupSelectorDialog(context: Context, listener: GroupSelectorListener) : Dialog(context) {

    private var _listener: GroupSelectorListener = listener

    val TYPE_JOIN_GROUP = "TYPE_JOIN_GROUP"
    val TYPE_CREATE_GROUP = "TYPE_CREATE_GROUP"
    private var selectedType = TYPE_CREATE_GROUP

    private var code: String = ""
    private var groupName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_group_selector)

        group_join.setOnClickListener {
            selectedType = TYPE_JOIN_GROUP
            initViewGroupJoin()

            group_join.visible(false)
            group_create.visible(false)

            group_code_view.visible(true)
        }

        group_create.setOnClickListener {
            selectedType = TYPE_CREATE_GROUP
            initViewGroupCreate()

            group_join.visible(false)
            group_create.visible(false)

            group_name_view.visible(true)
        }

        control_cancel.setOnClickListener {
            dismiss()
        }

        control_ok.setOnClickListener {

            when (selectedType) {
                TYPE_CREATE_GROUP -> {
                    if (group_name_view.text.toString().isNotEmpty()) {
                        groupName = group_name_view.text.toString()
                        _listener.createGroupByName(groupName)
                        dismiss()
                    } else {
                        Toast.makeText(context, context.getString(R.string.groups_screen_empty_name), Toast.LENGTH_SHORT).show()
                    }
                }
                TYPE_JOIN_GROUP -> {
                    if (group_code_view.text.toString().isNotEmpty()) {
                        code = group_code_view.text.toString()
                        _listener.connectToGroupByCode(code)
                        dismiss()
                    } else {
                        Toast.makeText(context, context.getString(R.string.groups_screen_empty_code), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initViewGroupCreate() {
        dialog_title.text = context.getString(R.string.groups_screen_new_group)
        connect_to_group_checkbox.setImageResource(R.drawable.ic_radio_off)
        create_new_group_checkbox.setImageResource(R.drawable.ic_radio_on)
    }

    private fun initViewGroupJoin() {
        dialog_title.text = context.getString(R.string.groups_screen_join_group)
        connect_to_group_checkbox.setImageResource(R.drawable.ic_radio_on)
        create_new_group_checkbox.setImageResource(R.drawable.ic_radio_off)
    }

    interface GroupSelectorListener {
        fun connectToGroupByCode(code: String)
        fun createGroupByName(name: String)
    }
}