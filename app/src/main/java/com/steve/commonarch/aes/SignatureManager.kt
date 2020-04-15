package com.steve.commonarch.aes

import com.google.gson.JsonObject

class SignatureManager {
    companion object {
        const val TAG = "SignatureManager"
        val secret = "############"//签名secret
        /**
         * 签名
         */
        fun sign(jsonObject: JsonObject): String {
            //构造签名串
            val build = StringBuilder()

            jsonObject.entrySet().sortedBy {
                //排序
                it.key
            }.forEach {
                build.append(it.key).append("=").append(it.value.asString).append("&")
                jsonObject.addProperty(it.key, it.value.asString)
            }

            build.append(secret)

            return MD5UtilsCash.getMD5(build.toString())//最后MD5加密
        }
    }
}