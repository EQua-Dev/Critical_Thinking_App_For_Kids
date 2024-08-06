/*
 * Copyright (c) 2024.
 * Luomy EQua
 * Under Awesomeness Studios
 */

package com.awesomenessstudios.schoolprojects.criticalthinkingappforkids

import android.app.Application
import androidx.multidex.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }
}