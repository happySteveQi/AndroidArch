package com.steve.commonarch.livedata

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.security.acl.Owner
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

object LiveDataBus {

    private val bus: ConcurrentHashMap<Class<*>, MutableLiveData<*>> by lazy {
        ConcurrentHashMap<Class<*>, MutableLiveData<*>>()
    }

    @MainThread
    inline fun <T> with(clazz: Class<T>): MutableLiveData<T> {
        return getLiveData(clazz)
    }

    inline fun <reified T> post(t: T) {
        getLiveData(T::class.java).postValue(t)
    }

    fun <T> getLiveData(clazz: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(clazz)) {
            bus[clazz] = MutableLiveData(clazz)
        }
        return bus[clazz] as MutableLiveData<T>
    }

    fun <T> remove(clazz: Class<T>) {
        if (bus.contains(clazz)) {
            bus.remove(clazz)
        }
    }

    fun getLiveDataVersion(liveData: MutableLiveData<*>): Int {
        val versionField = LiveData::class.java.getDeclaredField("mVersion")
        versionField.isAccessible = true
        return versionField.get(liveData) as Int
    }
}

fun <T> MutableLiveData<T>.observerNonStickyForever(onChange: (t: T) -> Unit) {
    observeForever(onChange)
}

fun <T> MutableLiveData<T>.observerNonSticky(owner: LifecycleOwner, onChange: (t: T) -> Unit) {
    observe(owner, LiveDataBusObserve(this, onChange))
}

class LiveDataBusObserve<T>(
    private val liveData: MutableLiveData<T>,
    val onChange: (t: T) -> Unit
) :
    Observer<T> {
    var mLastVersion: Int = LiveDataBus.getLiveDataVersion(liveData)
    private val mIsFirstVersion = AtomicBoolean(true)
    override fun onChanged(t: T) {
        if (mIsFirstVersion.compareAndSet(true, false)) {
            // 走一次
            val currentLiveDataVersion = LiveDataBus.getLiveDataVersion(liveData)
            if (mLastVersion >= currentLiveDataVersion) {
                return
            }
            mLastVersion = currentLiveDataVersion
            onChange(t)
        } else {
            onChange(t)
        }
    }

}
