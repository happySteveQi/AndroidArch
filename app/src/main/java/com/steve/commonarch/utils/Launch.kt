package com.steve.commonarch.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

object Launch {

    fun <T> launch(context: Context, clazz: Class<T>, intent: Intent? = null) {
        var intent0: Intent?
        when (intent) {
            null -> {
                intent0 = Intent()
                intent0.setClass(context, clazz)
            }
            else -> intent0 = intent
        }
        if (context !is Activity) {
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent0)
    }

    fun Activity.launchForResult(intent: Intent, requestCode: Int) {
        this.startActivityForResult(intent, requestCode)
    }
}