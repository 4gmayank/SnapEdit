package com.star.snapedit.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.star.snapedit.R

class SplashActivity : AppCompatActivity() {


    private val SPLASH_TIME : Long  = 2*1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity( Intent(this, DashboardActivity::class.java))
            finish()
        }, SPLASH_TIME)

    }

}