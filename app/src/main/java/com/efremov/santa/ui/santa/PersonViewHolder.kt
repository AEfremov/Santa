package com.efremov.santa.ui.santa

import android.view.View
import com.efremov.santa.core.common.BaseRecyclerViewHolder
import com.efremov.santa.data.GroupUserDto
import kotlinx.android.synthetic.main.item_person.view.*

class PersonViewHolder(val view: View) : BaseRecyclerViewHolder<GroupUserDto>(view) {

//    private val avatarResIds = listOf(
//        R.drawable.avatar_green,
//        R.drawable.avatar_red,
//        R.drawable.avatar_turquoise,
//        R.drawable.avatar_yellow
//    )

    override fun bindView(data: GroupUserDto, onItemClick: ((item: GroupUserDto, position: Int) -> Unit)?) {
        with(view) {
            var name = "${data.first_name} ${data.last_name}"
            if (data.is_owner) {
                name += " - главный"
            }
            person_name.text = name

            person_avatar.setImageResource(data.avatar.avatarResId)
//            val index = Random().nextInt(avatarResIds.size)
//            person_avatar.setImageResource(avatarResIds[index])
        }
    }
}