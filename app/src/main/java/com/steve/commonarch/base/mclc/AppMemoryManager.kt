package com.steve.commonarch.base.mclc

import com.steve.commonarch.base.net.NetBaseParamsManager


/**
 * 手机可用内存
 */
class AppMemoryManager {
    companion object {
        /**
         * app 最大可用内存
         */
        fun getMaxMemory():String{
            val runtime = Runtime.getRuntime()
            return format(runtime.maxMemory())
        }

        /**
         * app 当前可用内存
         */
        fun getAvaliableMemory():String{
            val runtime = Runtime.getRuntime()
            return format(runtime.totalMemory())
        }

        /**
         * app 可释放内存
         */

        fun getFreeMemory():String{
            val runtime = Runtime.getRuntime()
            return format(runtime.freeMemory())
        }

        /**
         * 小数点后两位
         */
        private fun format(value:Long):String{
            return NetBaseParamsManager.getFormatSize(value.toDouble())
        }
    }
}