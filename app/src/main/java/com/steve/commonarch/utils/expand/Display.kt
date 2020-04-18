package com.steve.commonarch.utils.expand

import android.content.Context
import com.steve.commonarch.App

/**
 * Created by weisl on 2019/10/15.
 */

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun dip2px(dpValue: Float): Int {
    val scale = App.getAppContext().resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dip(pxValue:Float):Int {
    val scale = App.getAppContext().resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}


fun dip2px(context: Context, dpValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun px2dip(context: Context, pxValue: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (pxValue / scale + 0.5f).toInt()
}