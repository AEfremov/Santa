package com.efremov.santa.core.common

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<T>(
        view: View
): RecyclerView.ViewHolder(view) {

    abstract fun bindView(
            data: T,
            onItemClick: ((item: T, position: Int) -> Unit)?
    )
}