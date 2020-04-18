package com.steve.commonarch.utils.expand

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.KeyListener
import android.text.method.NumberKeyListener
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.steve.commonarch.App
import com.steve.commonarch.AppEnv
import com.steve.commonarch.R
import kotlinx.android.synthetic.main.layout_toast_view.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Created by sunsg on 2018/2/3.
 */

const val DEBUG = AppEnv.DEBUG
const val TAG = "EXPAND"

private fun toast(
    message: CharSequence, @DrawableRes drawableLeft: Int = 0, @DrawableRes drawableTop: Int = 0,
    @DrawableRes drawableRight: Int = 0, @DrawableRes drawableBottom: Int = 0,
    duration: Int = Toast.LENGTH_SHORT
) {
    // try-catch解决在8.0版本以下NotificationManager可能取消Token,
    // 因此尝试添加View到Window的时候可能会出问题，只能通过Try-Catch解决(8.0以及以上版本已经解决和这个问题)
    try {
        val toast = Toast.makeText(App.getAppContext(), message, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = duration
        val view = LayoutInflater.from(App.getAppContext())
            .inflate(R.layout.layout_toast_view, null, false)
        view.tv_toast_text.text = message
        view.tv_toast_text.setCompoundDrawablesWithIntrinsicBounds(
            drawableLeft,
            drawableTop,
            drawableRight,
            drawableBottom
        )
        toast.view = view
        toast.show()
    } catch (e: Exception) {
        // Nothing to do
        if (AppEnv.DEBUG) {
            Log.e("debug_toast", "exception = $e")
        }
    }
}


var keyListener: KeyListener = object : NumberKeyListener() {
    /**
     * @return ：返回哪些希望可以被输入的字符,默认不允许输入
     */
    override fun getAcceptedChars(): CharArray {
        return App.getAppContext().getString(R.string.name_input_type).toCharArray()
    }

    /**
     * 0：无键盘,键盘弹不出来
     * 1：英文键盘
     * 2：模拟键盘
     * 3：数字键盘
     *
     * @return
     */
    override fun getInputType(): Int {
        return 1
    }
}

fun doubleToString(score: Double): String? {
    val nf: NumberFormat = NumberFormat.getInstance()
    nf.maximumIntegerDigits = 50
    nf.maximumFractionDigits = 50
    return nf.format(score)
}

fun startAnim(durationTime: Long = 2000, view: View) {
    val animator1 =
        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)

    val animator2 =
        ObjectAnimator.ofFloat(
            view,
            "translationY",
            view.y + dip2px(40f),
            view.y
        )

    animator1.duration = durationTime
    animator2.duration = durationTime

    val animatorSet1 = AnimatorSet()
    animatorSet1.playTogether(animator1, animator2)
    animatorSet1.start()

}

fun setSpannable(
    spannInfo: SpannableString,
    url: String,
    start: Int,
    end: Int,
    from: Int,
    @ColorRes colorId: Int = R.color.color_FFEDC976, hasUnderLine: Boolean = true
) {
    spannInfo.apply {
        setSpan(
            object : ClickableSpan() {

                override fun onClick(widget: View) {
//                    Launch.skipWebViewActivity(
//                        App.getAppContext(),
//                        url, from
//                    )
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = hasUnderLine
                }
            }, start, end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        setSpan(
            ForegroundColorSpan(
                App.getAppContext().resources.getColor(
                    colorId
                )
            ), start, end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

fun toast(@StringRes stringId: Int, @DrawableRes icon: Int = 0, duration: Int = Toast.LENGTH_SHORT) {
    // try-catch解决在8.0版本以下NotificationManager可能取消Token,
    // 因此尝试添加View到Window的时候可能会出问题，只能通过Try-Catch解决(8.0以及以上版本已经解决和这个问题)
    try {
        toast(App.getAppContext().getString(stringId), icon, duration)
    } catch (e: Exception) {
        // Nothing to do
    }
}

fun toast(str: String, @DrawableRes icon: Int = 0) {
    // try-catch解决在8.0版本以下NotificationManager可能取消Token,
    // 因此尝试添加View到Window的时候可能会出问题，只能通过Try-Catch解决(8.0以及以上版本已经解决和这个问题)
    try {
        toast(str, icon, Toast.LENGTH_SHORT)
    } catch (e: Exception) {
        // Nothing to do
    }
}

fun isEmpty(str: String?): Boolean {
    return str == null || str.isEmpty()
}

fun isNotEmpty(str: String): Boolean {
    return str.isNotEmpty()
}


/**
 * 隐藏软键盘
 */
fun hideSoftInput(activity: Activity) {
//    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(context
//            .currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        activity.window.decorView.windowToken,
        0
    )
}

/**
 * 显示软键盘
 * @param view
 */
fun showSoftInput(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}


/**
 * 是否是小米手机
 */
inline fun isXiaomi(): Boolean {
    return Build.BRAND.toUpperCase() == "XIAOMI"
}

/**
 * 是否是联想手机
 */
inline fun isLenovo(): Boolean {
    return Build.BRAND.toUpperCase() == "LENOVO"
}

fun EditText.getContent(): String {
    return text.toString().trim()
}

fun TextView.getContent(): String {
    return text.toString().trim()
}


/**
 * 判断一个字符串是否有数据
 */
fun isAvailable(str: String?): Boolean {
    return str != null && str.trim().isNotEmpty()
}

fun isOtpAvailable(str: String?): Boolean {
    if (str == null || str.trim().isEmpty()) {
        return false
    }
    return str.length == 4
}


/**
 * 判断一个单列集合是否有数据
 */
fun <T> isAvailable(list: Collection<T>?): Boolean {
    return list != null && !list.isEmpty()
}

/**
 * 校验手机号
 * 纯数字，10位，非0开头
 */
fun checkMobileNumber(str: String?): Boolean {
    if (str == null || str.trim().isEmpty()) {
        return false
    }
    if (str.startsWith("0")) {
        return false
    }
    return str.length == 10
}


/**
 * 以数字7、8、9开头
 */
fun checkMobilePattern(str: String): Boolean {
    return Pattern.compile("^(7|8|9)\\d{9}$").matcher(str).matches()
}



/**
 * 校验验证码
 * 4位数字
 */
fun checkCode(content: String?): Boolean {
    if (TextUtils.isEmpty(content)) return false
    val regex = "\\d{4}"
    return content!!.replace(" ", "").matches(Regex(regex))
}

/**
 * 截取字符串的最后4位
 */
fun getLastFourChar(str: String): String {
    return if (str.length >= 4) {
        str.substring(str.length - 4)
    } else {
        str
    }
}

const val FORMAT_DATE_PATTERN_1 = "dd/MM HH:mm"
const val FORMAT_DATE_PATTERN_2 = "dd/MM"
const val FORMAT_DATE_PATTERN_3 = "yyyy/MM/dd"
const val FORMAT_DATE_PATTERN_4 = "yyyy/MM/dd HH:mm:ss"
const val FORMAT_DATE_PATTERN_5 = "yyyy/MM/dd HH:mm"
const val FORMAT_DATE_PATTERN_6 = "d"

fun formatDate(millis: Long, pattern: String = FORMAT_DATE_PATTERN_1): String {
    return SimpleDateFormat(pattern).format(Date(millis))
}

fun formatDateNextMonth(millis: Long, pattern: String = FORMAT_DATE_PATTERN_1): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    calendar.add(Calendar.MONTH, 1)
    return SimpleDateFormat(pattern).format(Date(calendar.timeInMillis))
}

/**
 * 格式化金额
 */
fun formatPrice(price: Double): String {
    return DecimalFormat().let {
        // keep 2 decimal places
        it.maximumFractionDigits = 2
        it.minimumFractionDigits = 2
        it.groupingSize = 3
        it.roundingMode = RoundingMode.HALF_UP
        it.format(price)
    }
}

fun formatPriceKeep2Decimal(price: Double): Double {
    return BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
}

fun formatPriceKeep2DecimalPlaces(price: Double): String {
    return DecimalFormat("#.00").format(price)
}

fun formatNumber(num: Int) = DecimalFormat("00").format(num)

/**
 * 字符串首字母大写
 */
fun upperCaseFirstChar(content: String): String {
    val ch = content.toCharArray()
    if (ch[0] in 'a'..'z') {
        ch[0] = (ch[0].toInt() - 32).toChar()
    }
    return String(ch)
}

fun getFirstChar(content: String): String {
    val ch = content.toCharArray()
    if (ch[0] in 'a'..'z') {
        ch[0] = (ch[0].toInt() - 32).toChar()
    }
    return ch[0].toString()
}

/**
 * 打开网页首选系统浏览器
 */
fun openWebPagePreferSystemBrowser(url: String) {
    if (TextUtils.isEmpty(url))
        return
    if (!openUrlViaSystemBrowser(url)) {
//        Launch.skipWebViewActivity(App.getAppContext(), url, WebViewFragment.FROM_EXTERNAL_URL)
    }
}

/**
 * 通过系统浏览器打开网页
 */
fun openUrlViaSystemBrowser(url: String): Boolean {
    if (TextUtils.isEmpty(url))
        return false
    try {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val context = App.getAppContext()

        if (intent.resolveActivity(context.packageManager) != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            if (DEBUG) {
                Log.e(TAG, "can not find default browser")
            }
            return false
        }
    } catch (e: Exception) {
        if (DEBUG) {
            Log.e(TAG, "系统自动处理失败 $e")
            throw e
        }
        return false
    }
    return true
}

/**
 * 获取焦点并显示软键盘
 */
fun editGetFocus(editText: EditText) {
    if (DEBUG) Log.d(TAG, "---- editGetFocus -----")
    editText.isFocusable = true
    editText.isFocusableInTouchMode = true
    editText.requestFocus()
    editText.setSelection(editText.text.length)
    if (editText != null) {
        showSoftInput(editText)
    }
}

/**
 * 根据数字月份返回英文月份
 * ps：传入"03" 返回"March"
 */
fun getMonth(month: String): String {
    return when (month) {
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"
        "12" -> "Dec"
        else -> ""
    }

}

const val ALL_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
const val ALL_CHARACTERS_SIZE = ALL_CHARACTERS.length


/**
 * 复制文本到剪贴板
 */
fun copyClipboard(context: Context, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.text = text
    toast(R.string.copy_success)
}


fun isAllSame(inputContent: String?): Boolean {
    inputContent ?: return true// 空 不能用
    if (inputContent.isEmpty()) return true// 空 不能用
    val tmpChar = inputContent[0]
    for (i in 1 until inputContent.length) {
        if (DEBUG) Log.d(
            TAG,
            "isAllSame inputContent>> ${inputContent[i]} tmpChar >> $tmpChar inputContent.length >> ${inputContent.length}"
        )
        if (inputContent[i] != tmpChar) {
            return false
        }
    }
    return true
}

/**
 * 拨打电话-跳到拨号盘
 */
fun callPhone(context: Context, phoneNum: String) {
    try {
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNum")).apply {
            if (resolveActivity(context.packageManager) != null) { //resolveActivity不一定可靠
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this@apply)
            } else {
                if (DEBUG) {
                    Log.e(TAG, "can not resolve this intent")
                }
            }
        }
    } catch (e: Exception) {
        if (DEBUG) {
            Log.e(TAG, "callPhone failed", e)
        }
    }
}

/**
 * 发邮件
 */
fun sendEmail(context: Context, email: String) {
    try {
        Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).apply {
            if (resolveActivity(context.packageManager) != null) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(this@apply)
            } else {
                if (DEBUG) {
                    Log.e(TAG, "can not resolve this intent")
                }
            }
        }
    } catch (e: Exception) {
        if (DEBUG) {
            Log.e(TAG, "sendEmail failed", e)
        }
    }

}