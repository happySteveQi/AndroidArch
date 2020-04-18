package com.steve.commonarch.base.ui

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.steve.commonarch.AppEnv
import com.steve.commonarch.base.IActivityModel
import com.steve.commonarch.base.InjectorActivity
import com.steve.commonarch.custom.DefaultDialog
import com.steve.commonarch.livedata.observerNonSticky
import com.steve.commonarch.utils.expand.dismissDialog

open abstract class BaseActivity : InjectorActivity() {
    companion object {
        const val TAG = "BaseActivity"
        const val DEBUG = AppEnv.DEBUG
    }

    private var mLoadingDialog: DefaultDialog? = null
    protected var mCheckPermissionDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (initVariables(savedInstanceState, false)) {
            if (isNeedPortraitScreen()) {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            setContentView(getLayoutResID())
            initViews()
            observeLiveData()
            loadData()
        }
    }

    abstract fun loadData()
    /**
     * observe the livedata
     */
    open fun observeLiveData() {
        getActivityViewModel()?.getViewModel()?.loading?.observerNonSticky(this){
            if (it){
                showLoading()
            }else{
                hideLoading()
            }
        }

    }

    private fun showLoading() {

    }

    private fun hideLoading() {
        dismissDialog(mLoadingDialog)
    }

    private fun getActivityViewModel(): IActivityModel<*>? {
        return if (this is IActivityModel<*>) {
            this
        } else {
            null
        }

    }

    /**
     * initialize the widget, to set click event ect.
     */
    abstract fun initViews()

    @LayoutRes
    abstract fun getLayoutResID(): Int

    /**
     * force the screen to be portrait.
     */
    private fun isNeedPortraitScreen(): Boolean {
        return true
    }

    open fun initVariables(savedInstanceState: Bundle?, b: Boolean): Boolean {
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Toolbar have to set title after onCreate(),
        // or the default title will cover we set
        getToolbar()?.let {
            if (getTitleResId() == 0) {// No title is needed.
                it.title = ""
            } else {
                it.setTitle(getTitleResId())
            }
        }
    }

    open fun getToolbar(): Toolbar? = null

    open fun getTitleResId(): Int = 0

}