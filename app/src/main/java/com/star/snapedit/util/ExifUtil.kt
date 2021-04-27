package com.star.snapedit.util

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import android.text.TextUtils
import com.star.snapedit.cropview.util.Utils
import com.star.snapedit.model.InfoItem
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class ExifUtil {

    fun copyExifInfo(context: Context?, sourceUri: Uri?, saveUri: Uri?, outputWidth: Int,
                     outputHeight: Int): ArrayList<InfoItem> {
        var infoList :ArrayList<InfoItem> = ArrayList()
        if (sourceUri == null || saveUri == null) return infoList
        try {
            val sourceFile = Utils.getFileFromUri(context, sourceUri)
            val saveFile = Utils.getFileFromUri(context, saveUri)
            if (sourceFile == null || saveFile == null) {
                return infoList
            }
            val sourcePath = sourceFile.absolutePath
            val savePath = saveFile.absolutePath
            val sourceExif = ExifInterface(sourcePath)
            val tags: ArrayList<String> = ArrayList()
            tags.add(ExifInterface.TAG_DATETIME)
            tags.add(ExifInterface.TAG_FLASH)
            tags.add(ExifInterface.TAG_FOCAL_LENGTH)
            tags.add(ExifInterface.TAG_GPS_ALTITUDE)
            tags.add(ExifInterface.TAG_GPS_ALTITUDE_REF)
            tags.add(ExifInterface.TAG_GPS_DATESTAMP)
            tags.add(ExifInterface.TAG_GPS_LATITUDE)
            tags.add(ExifInterface.TAG_GPS_LATITUDE_REF)
            tags.add(ExifInterface.TAG_GPS_LONGITUDE)
            tags.add(ExifInterface.TAG_GPS_LONGITUDE_REF)
            tags.add(ExifInterface.TAG_GPS_PROCESSING_METHOD)
            tags.add(ExifInterface.TAG_GPS_TIMESTAMP)
            tags.add(ExifInterface.TAG_MAKE)
            tags.add(ExifInterface.TAG_MODEL)
            tags.add(ExifInterface.TAG_WHITE_BALANCE)
            tags.add(ExifInterface.TAG_EXPOSURE_TIME)

//        tags.add(ExifInterface.TAG_APERTURE);
//        tags.add(ExifInterface.TAG_ISO);
//        tags.add(ExifInterface.TAG_DATETIME_DIGITIZED);
            tags.add(ExifInterface.TAG_SUBSEC_TIME)

//        tags.add(ExifInterface.TAG_SUBSEC_TIME_DIG);
//        tags.add(ExifInterface.TAG_SUBSEC_TIME_ORIG);
            tags.add(ExifInterface.TAG_F_NUMBER)
            tags.add(ExifInterface.TAG_ISO_SPEED_RATINGS)
            tags.add(ExifInterface.TAG_SUBSEC_TIME_DIGITIZED)
            tags.add(ExifInterface.TAG_SUBSEC_TIME_ORIGINAL)
            val saveExif = ExifInterface(savePath)
            var value: String?
            for (tag in tags) {
                value = sourceExif.getAttribute(tag)
                if (!TextUtils.isEmpty(value)) {
                    saveExif.setAttribute(tag, value)
                    infoList.add(InfoItem(tag, value!!))
                }
            }
            saveExif.setAttribute(ExifInterface.TAG_IMAGE_WIDTH, outputWidth.toString())
            saveExif.setAttribute(ExifInterface.TAG_IMAGE_LENGTH, outputHeight.toString())
            saveExif.setAttribute(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED.toString())
            saveExif.saveAttributes()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return infoList
    }

}