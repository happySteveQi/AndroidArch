package com.steve.commonarch.aes

import android.util.Log
import java.net.URLDecoder
import java.net.URLEncoder
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by sunsg on 2017/9/6.
 */

class AESUtil {
    companion object {
        val TAG = "AESUtil"
        val DEBUG = true
        /**
         * AES加解密算法
         *
         * key 转换为 16位二进制（String 转换为数组）再base64，再URLencoder
         *
         * @author arix04
         */
        // 加密
        fun encrypt(sSrc: String, sKey: String,ivIn: String,needUrlEncoder:Boolean = true): String? {
            if (DEBUG) {
                Log.i(TAG, "encrypt Key = $sKey")
                Log.i(TAG, "encrypt src = $sSrc")
            }
            // 判断Key是否为16位
            val raw = ByteConvertor.hexStringToBytes(sKey)
            if (raw.size != 16) {
                if (DEBUG) {
                    Log.i(TAG, "encrypt Key length is not 16位")
                }
                return null
            }
            try {
                val skeySpec = SecretKeySpec(raw, "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")//"算法/模式/补码方式"
                val iv = IvParameterSpec(ByteConvertor.hexStringToBytes(ivIn))//使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
                val encrypted = cipher.doFinal(sSrc.toByteArray())
                var finalData = Base64Utils.encode(encrypted)//此处使用BASE64做转码功能
                if(needUrlEncoder){
                    finalData = URLEncoder.encode(finalData,"utf-8")//urlencoder
                }
                if (DEBUG) {
                    Log.d(TAG, "src encrypt final data = $finalData")
                }
                return finalData
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }



        //java 合并两个byte数组
        fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
            val byte_3 = ByteArray(byte_1.size + byte_2.size)
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
            System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
            return byte_3
        }

        // 解密
        fun decrypt(sSrc: String, sKey: String,ivOut: String,needUrlDecoder: Boolean = true): String? {
            try {
                if (DEBUG) {
                    Log.i(TAG, "decrypt Key = $sKey src = $sSrc")
                }
                // 判断Key是否为16位
                val raw = ByteConvertor.hexStringToBytes(sKey)
                if (raw.size != 16) {
                    if (DEBUG) {
                        Log.i(TAG, "decrypt Key length is not 16位")
                    }
                    return null
                }
                val skeySpec = SecretKeySpec(raw, "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                val iv = IvParameterSpec(ByteConvertor.hexStringToBytes(ivOut))
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)
                var urlDecoder = sSrc
                if(needUrlDecoder){
                    urlDecoder = URLDecoder.decode(sSrc,"utf-8")//URLDECODE
                }
                val encrypted1 = Base64Utils.decode(urlDecoder)//base64解密
                val original = cipher.doFinal(encrypted1)
                val finalReturn = String(original)
                if(DEBUG){
                    Log.i(TAG,"decrypt = $finalReturn")
                    Log.i("okhttp","decrypt = $finalReturn")
                }
                return finalReturn

            } catch (e: Exception) {
                if (DEBUG) {
                    Log.i(TAG, "e.toString() = $e")
                }
            }

            return null
        }
    }
}

