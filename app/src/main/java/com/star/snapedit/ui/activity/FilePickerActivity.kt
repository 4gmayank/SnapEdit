package com.star.snapedit.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.star.snapedit.R
import com.star.snapedit.ui.fragment.EditImageFragment

class FilePickerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_imgae_picker)
        val bundle: Bundle? = intent.getBundleExtra("bundle")
        if (savedInstanceState == null) {
            openFragment(EditImageFragment.newInstance(), bundle)
        }
    }


    private fun openFragment(fragment: Fragment, bundle: Bundle?) {
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow()

    }
}