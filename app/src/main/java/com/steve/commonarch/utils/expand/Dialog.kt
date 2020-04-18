package com.steve.commonarch.utils.expand

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.WindowManager
import com.steve.commonarch.App
import com.steve.commonarch.AppEnv
import com.steve.commonarch.R
import com.steve.commonarch.custom.DefaultDialog
import com.steve.commonarch.custom.dialog.DialogManager


fun Activity.showDialog(
    message: String?,
    left: String,
    right: String,
    leftListener: () -> Unit = {},
    rightListener: () -> Unit = {},
    view: View? = null,
    cancelable: Boolean = true
): Dialog? {
    if (isFinishing) {
        return null
    }
    return DialogManager.Builder().messageContent(message)
        .leftButton(left)
        .rightButton(right)
        .leftListener(leftListener)
        .rightListener(rightListener)
        .view(view)
        .isCancelable(cancelable)
        .builder()
        .show(this)
}

fun Activity.showDialog(
    messageId: Int,
    leftId: Int,
    rightId: Int,
    leftListener: () -> Unit = {},
    rightListener: () -> Unit = {}
): Dialog? {
    return showDialog(
        App.getAppContext().getString(messageId),
        App.getAppContext().getString(leftId),
        App.getAppContext().getString(rightId),
        leftListener,
        rightListener
    )
}

/**
 * 显示loading框
 */
fun Activity.showLoadingDialog(cancelable: Boolean = true): DefaultDialog? {
    val dialog = DefaultDialog(this)
    try {
        dialog.setContentView(R.layout.view_loading)
    } catch (e: Exception) {
        return null
    }
    val loading: View = dialog.findViewById(R.id.loading_root)
    loading.setOnClickListener {
        dialog.dismiss()
    }
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(false)
    showDialog(dialog)
    return dialog
}

/**
 * 创建loading dialog
 */
fun Activity.createLoadingDialog(cancelable: Boolean = true): DefaultDialog? {
    val dialog = DefaultDialog(this)
    try {
        dialog.setContentView(R.layout.view_loading)
        val window = dialog.window
        window.setBackgroundDrawableResource(R.color.transparent)//背景透明
        var layoutParams = window.attributes
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.dimAmount = 0.8f
        window.attributes = layoutParams
    } catch (e: Exception) {
        return null
    }
    val loading: View = dialog.findViewById(R.id.loading_root)
    loading.setOnClickListener {
        dialog.dismiss()
    }
    dialog.setCancelable(cancelable)
    dialog.setCanceledOnTouchOutside(false)
    return dialog
}

fun Activity.showDialog(dialog: Dialog?): Boolean {
    try {
        if (dialog == null || this.isFinishing || dialog.isShowing) {
            return false
        }
        dialog.show()
        return true
    } catch (t: Throwable) {
        if (AppEnv.DEBUG) {
            t.printStackTrace()
        }
    }
    return false
}
/**
 * close the dialog safely.
 *
 * @param dialog
 */
fun Activity.dismissDialog(dialog: Dialog?) {
    try {
        if (dialog == null/* || this.isFinishing*/) {
            return
        }

        if (dialog.isShowing) {
            dialog.dismiss()
        }
    } catch (t: Throwable) {
        if (AppEnv.DEBUG) {
            t.printStackTrace()
        }
    }

}