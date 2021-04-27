package com.star.snapedit.ui.fragment

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import com.star.snapedit.R
import com.star.snapedit.cropview.util.Utils
import com.star.snapedit.model.ImageMediaViewModel
import com.star.snapedit.model.InfoDetailViewModel
import com.star.snapedit.model.InfoItem
import com.star.snapedit.ui.dailog.InfoFragment
import com.star.snapedit.util.ExifUtil

class EditImageFragment : BasicFragment() {

    var selectedUri : Uri?= null

    companion object {
        fun newInstance() = EditImageFragment()
    }


    private lateinit var viewModel: ImageMediaViewModel
//    private var imgPreview : CropImageView? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val bundle :Bundle= arguments!!
        selectedUri = bundle.get("uri") as Uri?

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.edit_img_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT)
            mSourceUri = savedInstanceState.getParcelable(KEY_SOURCE_URI)
        }

        bindViews(view);
    }

    override fun bindViews(view: View) {
        super.bindViews(view)
        if(selectedUri != null) {
            mCropView?.setImageURI(selectedUri)
        }
        view.findViewById<View>(R.id.flipX).setOnClickListener(btnListener)
        view.findViewById<View>(R.id.flipY).setOnClickListener(btnListener)
        view.findViewById<View>(R.id.save).setOnClickListener(btnListener)
        view.findViewById<View>(R.id.info).setOnClickListener(btnListener)
    }

    val btnListener : View.OnClickListener = object : View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id) {
                R.id.flipX -> createFlippedBitmap(true, false)
                R.id.flipY -> createFlippedBitmap(false, true)
                R.id.info -> showExifInfo()
                R.id.save -> saveCroppedImage()
            }
        }

    }

    private fun saveCroppedImage() {
    }

    private fun showExifInfo() {
//        val inputStream : InputStream
//        val exif = ExifInterface(inputStream)
        val infoDetail : InfoDetailViewModel = InfoDetailViewModel()
        infoDetail.infoList.addAll(ExifUtil)

        val context :Context = activity?.applicationContext!!
        val bottomSheetDialogFragment : InfoFragment = InfoFragment(context, infoDetail)
        fragmentManager?.let { bottomSheetDialogFragment.show(it, "InfoDailog") }
    }


    fun createFlippedBitmap(xFlip: Boolean, yFlip: Boolean) {
        val source : Bitmap = this.mCropView?.drawable?.toBitmap() ?: return
        val matrix = Matrix()
        matrix.postScale(
                if (xFlip) -1f else 1f,
                if (yFlip) -1f else 1f,
                source.width / 2f,
                source.height / 2f
        )
        this.mCropView?.imageBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this, ViewModelProvider.Factory).get(ImageMediaViewModel::class.java)
        // TODO: Use the ViewModel
    }





}