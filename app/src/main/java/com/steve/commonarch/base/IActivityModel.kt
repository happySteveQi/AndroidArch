package com.steve.commonarch.base

/**
 * Created by luzhongxu on 2019-10-29.
 */
interface IActivityModel<T : BaseViewModel> {
    fun getViewModel(): T
}