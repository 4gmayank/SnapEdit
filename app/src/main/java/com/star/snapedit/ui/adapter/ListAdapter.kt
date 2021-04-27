package com.android.space.feature.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(context: Context?, list: ArrayList<*>?, rowCreator: ListRowCreator?, @LayoutRes resource: Int) : RecyclerView.Adapter<ListAdapter.SPViewHolder>() {
    private var objectList: ArrayList<*>? = null
    private var resource = 0
    private var rowCreator: ListRowCreator? = null
    private var context: Context? = null
    init {
        objectList = list
        this.resource = resource
        this.rowCreator = rowCreator
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SPViewHolder {
        val convertView = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        return SPViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: SPViewHolder, position: Int) {
        rowCreator!!.createItemRow(holder.itemView, objectList!![position])
    }

    override fun getItemCount(): Int {
        return if (objectList == null) 0 else objectList!!.size
    }

    fun setItems(list: ArrayList<*>) {
        this.objectList = list
        super.notifyDataSetChanged()
    }

    interface ListRowCreator {
        fun <T> createItemRow(rowView: View, rowObject: T)
    }

    class SPViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}