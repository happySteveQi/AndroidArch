package com.steve.commonarch.base.net

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import com.steve.commonarch.AppEnv
import com.steve.commonarch.base.BaseResponse
import com.steve.commonarch.livedata.InvalidTokenEvent
import com.steve.commonarch.livedata.LiveDataBus
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean


/**
 * Created by weisl on 2019/8/12.
 */
class LiveDataCall<T>(
    private val skipLogin: Boolean = true,
    private val isUseDefaultTips: Boolean = true,
    private val clazz: Class<T>,
    private val flowable: () -> Flowable<BaseResponse<T>>,
    private val disposableList: ArrayList<Disposable>?
    ) : LiveData<BaseResponse<T>>() {
    private var started = AtomicBoolean(false)

    @SuppressLint("CheckResult")
    override fun onActive() {
        if (!started.compareAndSet(false, true)) return
        val disposable = Flowable.just(0)
            .flatMap {
                flowable()
            }.doOnNext {
                it.parseT(clazz)
                if (!it.isSuccess()) {
                    throw HttpResponseException(it.code, it.t, it.message)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postValue(it)
            }, { throwable ->
                if (AppEnv.DEBUG) {
                    Log.e("OkHttp", "LiveDataCall onError:$throwable")
                }
                ApiExceptionHandler.exceptionTransformResponse<T>(throwable).apply {
                    if (isInvalidToken() && skipLogin) {
                        LiveDataBus.post(InvalidTokenEvent())
                    }
                    //统一处理错误提示
                    ApiExceptionHandler.showToast(this@apply, isUseDefaultTips)
                    postValue(this@apply)
                }
            })
        disposableList?.add(disposable)
    }
}