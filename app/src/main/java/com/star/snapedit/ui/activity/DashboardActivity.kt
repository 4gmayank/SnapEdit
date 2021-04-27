package com.star.snapedit.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.star.snapedit.R

class DashboardActivity : AppCompatActivity() {

    private var switch : ToggleButton?= null
    private var pickerBtn : Button?= null
    private var previewImg : ImageView?= null


    companion object {
        private const val PERMISSION_REQ_READ_EXTERNAL_STORAGE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_screen)
        switch = findViewById(R.id.image_picker_switch)
        previewImg = findViewById(R.id.image_preview)
        pickerBtn = findViewById(R.id.open_image_btn)

    }

    fun openFileManager(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getImageUri()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQ_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == PERMISSION_REQ_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageUri()
            }
        }else{
            Toast.makeText(this, "External Storage Read Permission is Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==122 && resultCode == RESULT_OK && data?.data != null){
            val bundle: Bundle = Bundle()
            val uri :Uri = data.data!!
            bundle.putParcelable("uri", uri)
            openFilePicker(bundle)
        }
    }

    private fun getImageUri() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_TITLE, "Select Image");
            startActivityForResult(intent, 122)
    }

    private fun openFilePicker(uri: Bundle?){
        val intent : Intent = Intent(this, FilePickerActivity::class.java)
        intent.putExtra("bundle",uri)
        startActivity(intent)
    }


    private var counter : Int = 0
    fun visibleSwitch(view: View) {
        if(++counter>5){
            switch?.visibility= View.VISIBLE
            switch?.isActivated = true
        }

    }

}