package com.star.snap.util
//
//import android.content.Context
//import android.database.Cursor
//import android.net.Uri
//import android.provider.MediaStore
//
//object AppUtil {
//
//
//    //Courotine work
//
//
//
//    fun loadAllImages()
//    {
//        viewModelScope.launch {
//            _allImagesFromGallery.value = withContext(Dispatchers.IO) {
//                getAllImages()
//            }
//        }
//    }
//
//    fun  imageList(context: Context, maxImage:Int): ArrayList<Uri>{
//
//        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        val cursor: Cursor?
//        val columnIndexID: Int
//        val imageUriList: MutableList<Uri> = mutableListOf()
//        val projection = arrayOf(MediaStore.Images.Media._ID)
//        var imageId: Long
//        cursor = context.contentResolver.query(uriExternal, projection, null, null, null)
//        if (cursor != null && imageUriList.size <maxImage) {
//            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
//            while (cursor.moveToNext()) {
//                imageId = cursor.getLong(columnIndexID)
//                val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
//                imageUriList.add(uriImage)
//            }
//            cursor.close()
//        }
//
//
//        return imageUriList as ArrayList<Uri>
//    }
//
//
//
//}