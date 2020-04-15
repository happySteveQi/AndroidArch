package com.steve.commonarch.aes

import android.util.Log
import com.project.Constant
import java.net.URLDecoder
import java.net.URLEncoder
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// 通用AES
class AESNormalUtil {
    companion object {
            //app 通用iv
            val IV_APP_NORMAL = Constant.IV_APP_NORMAL
            //app 通用key
            val KEY_APP_NORMAL = Constant.KEY_APP_NORMAL
            /**
             * cash loan 通用加密
             */
            fun encrypt(src: String, needUrlEncoder: Boolean = true): String? {
                return AESUtil.encrypt(src, KEY_APP_NORMAL, IV_APP_NORMAL, needUrlEncoder)
            }

            /**
             * cash loan 通用解密
             */
            fun decrypt(src: String, needUrlDecoder: Boolean = true): String? {
                return AESUtil.decrypt(src, KEY_APP_NORMAL, IV_APP_NORMAL, needUrlDecoder)
        }
    }
}