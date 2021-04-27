package com.star.snapedit.model

import android.net.Uri
import androidx.lifecycle.ViewModel

class ImageMediaViewModel(uri: Uri) : ViewModel() {
    // TODO: Implement the ViewModel

    var imgUri :Uri = uri

    init {
        imgUri = uri
    }
}