package com.efremov.santa.core.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseRecyclerViewAdapter<T>(
    val layout: Int = 0,
    var items: MutableList<T>,
    private val holderFactory: ((view: View) -> BaseRecyclerViewHolder<T>),
    private val onItemClick: ((item: T, position: Int) -> Unit)? = null
) : RecyclerView.Adapter<BaseRecyclerViewHolder<T>>() {

    open fun obtainLayout(viewType: Int): Int {
        return layout
    }

    override fun getItemCount() = items.size

    private fun inflateItemView(parent: ViewGroup?, viewType: Int): View {
        val inflater = LayoutInflater.from(parent?.context)
        return inflater.inflate(obtainLayout(viewType), parent, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : BaseRecyclerViewHolder<T> {
        return holderFactory.invoke(inflateItemView(parent, viewType))
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<T>, position: Int) {
        val item = items[position]
        holder.bindView(item, onItemClick)
    }

    fun addItem(item: T) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItems(items: List<T>) {
        this.items.clear()
        items.forEach { this.items.add(it) }
//        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }
}