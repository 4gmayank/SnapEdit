package com.star.snapedit.ui.dailog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.space.feature.list.ListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.star.snapedit.R
import com.star.snapedit.model.InfoDetailViewModel
import com.star.snapedit.model.InfoItem

class InfoFragment(context: Context, infoDetail: InfoDetailViewModel) : BottomSheetDialogFragment(), ListAdapter.ListRowCreator {
    var infoDetail : InfoDetailViewModel ?= null

    init {
        this.infoDetail = infoDetail
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.media_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView :RecyclerView = view.findViewById(R.id.info_list)
        recyclerView.adapter = ListAdapter(context, infoDetail?.infoList, this, R.layout.detail_item_row)

    }

    override fun <T> createItemRow(rowView: View, rowObject: T) {
        val infoItem :InfoItem = rowObject as InfoItem
        val headerTitle : TextView? = view?.findViewById(R.id.header)
        val detailText : TextView? = view?.findViewById(R.id.detail)
        headerTitle?.text = infoItem.header
        detailText?.text = infoItem.detail


    }

}