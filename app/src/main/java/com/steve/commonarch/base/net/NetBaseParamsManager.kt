package com.steve.commonarch.base.net

import android.os.Build
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import com.appsflyer.AppsFlyerLib
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.steve.commonarch.data.local.SharedPref
import com.steve.commonarch.data.local.SharedPrefKeyManager
import com.steve.commonarch.App
import com.steve.commonarch.AppEnv
import com.steve.commonarch.aes.AESNormalUtil
import com.steve.commonarch.aes.MD5UtilsCash
import com.steve.commonarch.aes.SignatureManager
import com.steve.commonarch.base.dot.DisplayUtils
import com.steve.commonarch.base.mclc.AppMemoryManager
import com.steve.commonarch.base.mclc.BatteryManager
import com.steve.commonarch.utils.NetWorkUtil
import com.steve.commonarch.utils.SysUtils
import okhttp3.Request
import okhttp3.internal.Version
import java.io.BufferedReader
import java.io.FileReader
import java.math.BigDecimal
import java.util.*

/**

 * 网络请求公共参数管理类
 */
class NetBaseParamsManager {
    companion object {

        const val DEBUG = AppEnv.DEBUG
        const val TAG = ""


        /**
         * 构造data参数
         */
        fun buildDataParams(jsonObject: JsonObject?, isNeedBase: Boolean = true): String {
            try {
                //1.添加必填签名信息
                getMustCashMustParams(jsonObject!!)
                //添加base签名信息
                if (isNeedBase) {
                    getCashNormalBaseParams(jsonObject)
                }
                //2.添加签名json
                jsonObject.addProperty("signature", SignatureManager.sign(jsonObject))
                return AESNormalUtil.encrypt(jsonObject.toString(), false).orEmpty()
            } catch (e: Exception) {
                if (DEBUG) {
                    Log.d("BaseRepository", "buildDataParams error = $e")
                }
                return ""
            }
        }

        /**
         * 格式化手机号
         */
        fun formatPhone(phoneNumber: String): String {
            return phoneNumber.replace(" ", "")
        }

        /**
         * md5 imei
         */
        fun md5IMEI(imie: String): String {
            return MD5UtilsCash.getMD5(imie) ?: ""
        }

        fun getTotalMemory(): String {
            val str1 = "/proc/meminfo"// 系统内存信息文件
            val str2: String
            val arrayOfString: Array<String>
            var initial_memory: Long = 0

            try {
                val localFileReader = FileReader(str1)
                val localBufferedReader = BufferedReader(
                    localFileReader, 8192
                )
                str2 = localBufferedReader.readLine()// 读取meminfo第一行，系统总内存大小

                arrayOfString =
                    str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//                for (num in arrayOfString) {
//                    Log.i(str2, num + "\t")
//                }

                initial_memory = (arrayOfString[1]).toLong() * 1024// 获得系统总内存，单位是KB，乘以1024转换为Byte
                localBufferedReader.close()

            } catch (e: Exception) {
            }
            return getFormatSize(
                initial_memory.toDouble()
            )// Byte转换为KB或者MB，内存大小规格化
        }

        /**
         * 格式化单位
         * @param size
         * @return
         */
        fun getFormatSize(size: Double): String {
            val kiloByte = size / 1024
            if (kiloByte < 1) {
                //            return size + "Byte";
                return "0K"
            }

            val megaByte = kiloByte / 1024
            if (megaByte < 1) {
                val result1 = BigDecimal(java.lang.Double.toString(kiloByte))
                return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K"
            }

            val gigaByte = megaByte / 1024
            if (gigaByte < 1) {
                val result2 = BigDecimal(java.lang.Double.toString(megaByte))
                return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M"
            }

            val teraBytes = gigaByte / 1024
            if (teraBytes < 1) {
                val result3 = BigDecimal(java.lang.Double.toString(gigaByte))
                return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB"
            }
            val result4 = BigDecimal(teraBytes)
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
        }

        /**
         * 获取length 随机数
         */
        fun getRandomString(length: Int): String { //length表示生成字符串的长度
            val base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val random = Random()
            val sb = StringBuilder()
            for (i in 0 until length) {
                val number = random.nextInt(base.length)
                sb.append(base[number])
            }
            return sb.toString()
        }

        /**
         * 获取 cash 通用 base params
         */
        fun getCashNormalBaseParams(jsonObject: JsonObject) {
            jsonObject.addProperty("os", "android ${Build.VERSION.RELEASE}")
            jsonObject.addProperty("imei", SysUtils.getDeviceId(App.getAppContext()))
            jsonObject.addProperty("uuid", SysUtils.generateCashUUID(App.getAppContext()))
            jsonObject.addProperty("model",Build.MODEL)
            jsonObject.addProperty("brand",Build.BRAND)
//            val locationInfo = LocationHelp.getLocationInfo()
//            jsonObject.addProperty("longitude", locationInfo?.longitude?.toString() ?: "")//经度
//            jsonObject.addProperty("latitude", locationInfo?.latitude?.toString() ?: "")//纬度
            jsonObject.addProperty("time_zone", TimeZone.getDefault().id)//时区
            jsonObject.addProperty(
                "network",
                NetWorkUtil.getNetworkState(App.getAppContext()).toString()
            )
            jsonObject.addProperty(
                "is_simulator", if (isSimulator()) {
                    "1"
                } else {
                    "0"
                }
            )//是否是模拟器登录
            jsonObject.addProperty("platform", "android")
            jsonObject.addProperty("city", "")
        }


        /**
         * 获取cash 必传参数
         */
        fun getMustCashMustParams(jsonObject: JsonObject) {
            jsonObject.addProperty("app_version", AppEnv.APP_VERSION)
            jsonObject.addProperty("app_version_code", AppEnv.getAppVersionCode())

            jsonObject.addProperty(
                "noise",
                getRandomString(
                    Random().nextInt(16) + 16
                )
            )//随机串,噪音元素 (16-32)位
            jsonObject.addProperty("request_time", System.currentTimeMillis().toString())
//            jsonObject.addProperty("access_token", getToken())
            jsonObject.addProperty("ui_version", AppEnv.UIVERSION.toString())
            jsonObject.addProperty("cid", AppEnv.getCID(App.getAppContext()))
            jsonObject.addProperty("is_google_service", 1)
            jsonObject.addProperty("system_language", "en")
//            jsonObject.addProperty("trace_id", createTraceId()) //增加trace_id
            addThirdSdkIdParams(jsonObject)
        }

        //trace_id 定义规范 https://pha.i.mobigroup.cn/w/cardbean/trace_id/
//        private fun createTraceId() =
//            "${SimpleDateFormat("MMddHHmmssS").format(Date())}_${getAccountIdKeepLastFiveDigits()}_ANDROID"

        /**
         * 添加第三方ID参数(fcm，appsflyer_id, google_advertising_id)
         */
        private fun addThirdSdkIdParams(jsonObject: JsonObject) {
            val fcmToken = FirebaseInstanceId.getInstance().token.orEmpty()
            jsonObject.addProperty("fcm_token", fcmToken)
            //记录fcmToken是否上传成功
            SharedPref.setBoolean(
                SharedPrefKeyManager.KEY_IS_UPLOAD_FCM_TOKEN_AUTH_SUCCESS,
                !TextUtils.isEmpty(fcmToken)
            )

            try {
                val appsFlyerId =
                    AppsFlyerLib.getInstance().getAppsFlyerUID(App.getAppContext()).orEmpty()
                jsonObject.addProperty("appsflyer_id", appsFlyerId)
            } catch (e: Exception) {
                jsonObject.addProperty("appsflyer_id", "")
            }
        }

        /**
         * user-agent
         */
        fun getUserAgent(): String {
            return "${Version.userAgent()} (Android ${Build.VERSION.RELEASE}) /v/1.5.2"
        }

        /**
         * 是否是模拟器
         * 现在默认是false
         */
        fun isSimulator(): Boolean {
            return false
        }

        //添加header信息
        fun addHeader(builder: Request.Builder) {
            builder.addHeader("App-Language", "us")
            builder.addHeader(
                "User-Agent",
                getUserAgent()
            )
            builder.addHeader("x-app-version", AppEnv.APP_VERSION)
            builder.addHeader("x-app-version-code", "${AppEnv.getAppVersionCode()}")
            builder.addHeader("x-sys-version", Build.VERSION.RELEASE)
            builder.addHeader("x-platform", "android")
            builder.addHeader("x-device", "${Build.BRAND}${Build.MODEL}")
            builder.addHeader("x-client-pixel", "1080x1920")//分辨率
            builder.addHeader(
                "x-gaid",
                SysUtils.getDeviceId(App.getAppContext())
            )//gaid 现在代表deveice id
            builder.addHeader("x-channel", "45545")//渠道
//            builder.addHeader("x-utm-source", AppFlyerManager.getAppsFlyerUtmSource())//AppsFlyer返回的渠道名称：例如facebook
//            builder.addHeader("x-appflyer-uid", AppsFlyerManager.getAppsFlyerUid())//appsflyer_uid
        }


        /**
         * 获取短信 通话记录 联系人base params
         */
        fun getMessageCallLogContactsBaseParams(): JsonObject {
            val jsonObject = JsonObject()
            try {
//                val locationInfo = LocationHelp.getLocationInfo()
                jsonObject.addProperty("os", "android ${Build.VERSION.RELEASE}")//操作系统和版本
                jsonObject.addProperty("imei", SysUtils.getDeviceId(App.getAppContext()))
                jsonObject.addProperty("uuid", SysUtils.generateCashUUID(App.getAppContext()))
                //经度
//                val longitude = locationInfo?.longitude?.toString()
//                longitude?.let { jsonObject.addProperty("lon", it) }
                //纬度
//                val latitude = locationInfo?.latitude?.toString()
//                latitude?.let { jsonObject.addProperty("lat", it) }
                jsonObject.addProperty(
                    "mem",
                    getTotalMemory()
                )//手机内存
                jsonObject.addProperty("model", android.os.Build.MODEL)//手机型号
                jsonObject.addProperty("brand", android.os.Build.BRAND)//手机品牌
                jsonObject.addProperty("is_tt", "0")
                jsonObject.addProperty(
                    "is_sim", if (
                        isSimulator()
                    ) {
                        "1"
                    } else {
                        "0"
                    }
                )//是否是模拟器登录
                jsonObject.addProperty("tz", TimeZone.getDefault().id)//时区
                jsonObject.addProperty("net", NetWorkUtil.getNetworkState(App.getAppContext()))
                jsonObject.addProperty("uptime", System.currentTimeMillis() / 1000)
//                jsonObject.addProperty("access_token", getToken())//token
                jsonObject.addProperty(
                    "phone_num", SysUtils.getPhoneNumber(App.getAppContext())
                        ?: ""
                )//手机号

                jsonObject.addProperty("gaid", AppEnv.GAID)//gaid
//                jsonObject.addProperty("ui_version", AppEnv.UIVERSION.toString())//uiveision
                jsonObject.addProperty(
                    "version_code",
                    AppEnv.getAppVersionCode().toString()
                )//versioncode
                jsonObject.addProperty("cid", AppEnv.getCID(App.getAppContext()))//versioncode
//                jsonObject.addProperty("event_id", EventKeyManager.EVENT_ACTIVATION)//事件id
                val screenWH = DisplayUtils.getScreenWH()
                jsonObject.addProperty("width", screenWH[0])
                jsonObject.addProperty("height", screenWH[1])
                jsonObject.addProperty("cpu_num", DisplayUtils.getCpuNum())

                jsonObject.addProperty("battery_level", BatteryManager.getLevelBattery())//剩余电量
                jsonObject.addProperty("battery_max", BatteryManager.getMaxBattery())//最大电量

                jsonObject.addProperty(
                    "total_boot_time",
                    SystemClock.elapsedRealtime().toString()
                )//开机总时长 单位微妙
                jsonObject.addProperty(
                    "total_boot_time_wake",
                    SystemClock.uptimeMillis().toString()
                )//开机总时长 非休眠时间 单位微妙

                jsonObject.addProperty(
                    "app_max_memory",
                    AppMemoryManager.getMaxMemory()
                )//app 可用最大内存
                jsonObject.addProperty(
                    "app_avaliable_memory",
                    AppMemoryManager.getAvaliableMemory()
                )//app 当前可用内存
                jsonObject.addProperty(
                    "app_free_memory",
                    AppMemoryManager.getFreeMemory()
                )//app 可释放内存

                jsonObject.addProperty("manufacturer", Build.MANUFACTURER)//厂商名
                jsonObject.addProperty("product", Build.PRODUCT)//产品名
                jsonObject.addProperty("board", Build.BOARD)//手机主板名
                jsonObject.addProperty("device", Build.DEVICE)//设备名
            } catch (e: Exception) {
                Log.e("debug_netBaseParams", "BaseParams e $e")
            }
            if (AppEnv.DEBUG) {
                Log.i("debug_netBaseParams", " getMessageCallLogContactsBaseParams = $jsonObject")
            }
            return jsonObject
        }
    }
}