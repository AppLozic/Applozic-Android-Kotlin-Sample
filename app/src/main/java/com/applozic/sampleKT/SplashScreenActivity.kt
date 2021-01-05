package com.applozic.sampleKT

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.applozic.mobicomkit.Applozic

class SplashScreenActivity : AppCompatActivity() {
    private val splashDisplayLength: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            if (Applozic.isConnected(this@SplashScreenActivity)) {
                val intent = Intent(
                    this, MainActivity::class.java
                )
                this.startActivity(intent)
                this.finish()
            } else {
                val intent = Intent(
                    this@SplashScreenActivity, LoginActivity::class.java
                )
                this@SplashScreenActivity.startActivity(intent)
            }
            this@SplashScreenActivity.finish()
        }, splashDisplayLength)
    }
}