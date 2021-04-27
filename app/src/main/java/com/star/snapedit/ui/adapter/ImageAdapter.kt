package com.star.snapedit.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.star.snapedit.R

class ImageAdapter(context: Context, list: ArrayList<*>?, rowCreator: ListRowCreator?, @LayoutRes resource: Int) : BaseAdapter(){
    private var objectList: ArrayList<*>? = null
    private var resource = 0
    private var context :Context?=null


    private var rowCreator: ListRowCreator? = null


    init {
        this.context = context
        objectList = list
        this.resource = resource
        this.rowCreator = rowCreator
    }

    override fun getCount(): Int {
        return objectList?.size?:0
    }

    override fun getItem(position: Int): Any {
        return objectList?.get(position) ?: ""
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View, parent: ViewGroup?): View {

        val convertVie = layoutInflater.inflate(R.layout.img_item, null)
        return rowCreator?.createItemRow(context, convertVie,objectList?.get(position)) as View
    }



    fun setItems(list: ArrayList<*>) {
        this.objectList = list
        super.notifyDataSetChanged()
    }


    interface ListRowCreator {
        fun <T> createItemRow(rowView: Context?, view : View,rowObject: T): ImageView
    }
}