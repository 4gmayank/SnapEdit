package com.star.snapedit.ui.fragment

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.star.snapedit.R
import com.star.snapedit.model.ImageMediaViewModel
import com.star.snapedit.model.PickerMediaViewModel
import com.star.snapedit.ui.adapter.ImageAdapter

class PickerImageFragment : Fragment(), ImageAdapter.ListRowCreator{


    private var mediaList : ArrayList<ImageMediaViewModel> = ArrayList()
    companion object {
        fun newInstance() = PickerImageFragment()
    }


    private lateinit var viewModel: PickerMediaViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  path :Uri= Uri.parse("android.resource://"+ activity?.packageName + R.drawable.app_preview);
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))
        mediaList.add(ImageMediaViewModel(path))




    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.picker_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val gridView : GridView = view.findViewById(R.id.picker_grid)
        gridView.adapter = ImageAdapter(activity!!.baseContext, mediaList, this, 0)
    }

    override fun <T> createItemRow(rowView: Context?, view :View,rowObject: T): ImageView {
        val uri = rowObject as ImageMediaViewModel
        val imageView : ImageView =  ImageView(rowView)
        imageView.setImageURI(uri.imgUri)
        return imageView
    }


}