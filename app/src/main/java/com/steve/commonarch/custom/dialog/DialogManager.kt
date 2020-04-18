package com.steve.commonarch.custom.dialog

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.view.WindowManager
import com.steve.commonarch.R
import com.steve.commonarch.utils.expand.showDialog

/**
 * Created by sunsg on 2018/3/3.
 */
class DialogManager {
    private var dTitle: String = ""
    private var messageContent: String? = ""
    private var leftButton: String = ""
    private var rightButton: String = ""
    private var leftListener: () -> Unit = {}
    private var rightListener: () -> Unit = {}
    private var alertDialog: AlertDialog? = null
    private var isCancelable: Boolean = true
    private var view: View? = null
    private var onDismissListener: (dialog: DialogInterface?) -> Unit = {}

    constructor(builder: Builder) {
        this.messageContent = builder.dmessageContent
        this.leftButton = builder.dleftButton
        this.rightButton = builder.drightButton
        this.leftListener = builder.dleftListener
        this.rightListener = builder.drightListener
        this.dTitle = builder.dTitleId
        this.isCancelable = builder.isCancelable
        this.view = builder.view
        this.onDismissListener = builder.onDismissListener
    }

    fun show(activity: Activity): AlertDialog? {

        val builder = AlertDialog.Builder(activity)
            .setTitle(dTitle)
            .setMessage(messageContent)
            .setCancelable(isCancelable)
            .setNegativeButton(leftButton) { dialog, which ->
                leftListener()
            }
            .setPositiveButton(rightButton) { dialog, which ->
                rightListener()
            }
            .setOnDismissListener(onDismissListener)
        if (view != null) {
            builder.setView(view)
        }
        alertDialog = builder.create()
        activity.showDialog(alertDialog)
        alertDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(activity.resources.getColor(R.color.color_8A000000))
        alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(activity.resources.getColor(R.color.colorPrimaryDark))
        val lp: WindowManager.LayoutParams = alertDialog?.window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT //设置宽度

        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        alertDialog?.window?.attributes = lp
        return alertDialog
    }

    fun setOnKeyListener(onKeyListener: DialogInterface.OnKeyListener) {
        alertDialog?.setOnKeyListener(onKeyListener)
    }

    fun dimiss() {
        alertDialog?.dismiss()
    }


    class Builder {
        var dTitleId: String = ""
        var dmessageContent: String? = ""
        var dleftButton: String = ""
        var drightButton: String = ""
        var dleftListener: () -> Unit = {}
        var drightListener: () -> Unit = {}
        var isCancelable: Boolean = true
        var view: View? = null
        var onDismissListener: (dialog: DialogInterface?) -> Unit = {}

        fun title(title: String): Builder {
            this.dTitleId = title
            return this
        }

        fun messageContent(messageContent: String?): Builder {
            this.dmessageContent = messageContent
            return this
        }

        fun leftButton(leftButton: String): Builder {
            this.dleftButton = leftButton
            return this
        }

        fun rightButton(rightButton: String): Builder {
            this.drightButton = rightButton
            return this
        }

        fun leftListener(leftListener: () -> Unit): Builder {
            this.dleftListener = leftListener
            return this
        }

        fun rightListener(rightListener: () -> Unit): Builder {
            this.drightListener = rightListener
            return this
        }

        fun isCancelable(isCancelable: Boolean): Builder {
            this.isCancelable = isCancelable
            return this
        }

        fun view(view: View?): Builder {
            this.view = view
            return this
        }

        fun onDismissListener(onDismissListener: (dialog: DialogInterface?) -> Unit): Builder {
            this.onDismissListener = onDismissListener
            return this
        }

        fun builder(): DialogManager {
            return DialogManager(this)
        }
    }

}