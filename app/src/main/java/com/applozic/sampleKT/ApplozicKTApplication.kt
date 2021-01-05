package com.applozic.sampleKT

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class ApplozicKTApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(baseContext)
    }
    
}