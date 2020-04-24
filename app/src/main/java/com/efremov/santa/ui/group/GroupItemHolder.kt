package com.efremov.santa.ui.group

import android.view.View
import com.efremov.santa.core.common.BaseRecyclerViewHolder
import com.efremov.santa.data.GroupDto
import kotlinx.android.synthetic.main.item_group.view.*

class GroupItemHolder(val view: View) : BaseRecyclerViewHolder<GroupDto>(view) {

    override fun bindView(data: GroupDto, onItemClick: ((item: GroupDto, position: Int) -> Unit)?) {
        with(view) {
            group_name_view.text = data.name
        }
        view.setOnClickListener { onItemClick?.invoke(data, adapterPosition) }
    }
}