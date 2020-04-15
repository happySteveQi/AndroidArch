package com.steve.commonarch.data.local

/**
 * Created by weisl on 2019/10/14.
 */
object SharedPrefKeyManager {
    const val CASH_KEY_REQUEST_CICY_TIMER: String = "request_cicy_timer"
    const val CASH_KEY_REQUEST_LOCATION_TIMER: String = "request_location_timer"
    //经纬度获取城市
    const val CASH_KEY_REQUEST_CITY = "cash_key_request_city"
    const val CASH_KEY_LOCATION_INFO: String = "location_info"

    const val CURRENT_KEY_MOBILE: String = "current_key_mobile"

    const val CASH_KEY_POST_MCLC: String = "post_mclc_0314"
    const val KEY_CID = "key_cid"
    const val PAY_ACCESS_TOKEN = "pay_access_token"
    const val PAY_ACCOUNT_ID = "pay_account_id"
    const val PAY_MOBILE = "pay_mobile"
    const val PAY_KEY_LOGIN = "pay_key_login"
    const val KEY_HOME_STATUS = "key_home_status"
    const val KEY_PROFILE = "key_profile"
    const val KEY_GUIDE = "key_guide"
    const val KEY_SUPPORT = "key_support"

    /**
     * 是否允许获取IMEI
     */
    const val KEY_AGREE_GET_IMEI = "key_agree_get_imei"

    /**
     * IMEI弹窗点击拒绝的次数
     */
    const val KEY_DIALOG_IMEI_DISAGREE_TIMES = "key_dialog_imei_disagree_times"

    /**
     * IMEI是否勾选“不再弹出”
     */
    const val KEY_DIALOG_IMEI_DONT_SHOW_AGAIN = "key_dialog_imei_dont_show_again"

    const val KEY_CACHE_FILE_PATH = "sd_file_path"

    const val KEY_NOTIFICATION_SWITCH = "key_notification_switch"
    const val KEY_CACHE_BILL_OVERVIEW = "key_cache_bill_overview"
    const val KEY_CACHE_CARD_OVERVIEW = "key_cache_card_overview"

    const val KEY_IS_UPLOAD_FCM_TOKEN_AUTH_SUCCESS = "key_is_upload_fcm_token_auth_success"
}