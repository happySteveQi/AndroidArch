package com.steve.commonarch.utils

import android.util.Log
import com.steve.commonarch.AppEnv
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class ThreadPoolUtil {
    companion object {
        const val TAG = "ThreadPoolUtil"
        const val DEBUG = AppEnv.DEBUG
        private const val CORE_POOL_SIZE = 5
        private const val MAXIMUM_POOL_SIZE = 128
        private const val KEEP_ALIVE = 1

        private val sThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)

            override fun newThread(r: Runnable?): Thread {
                return Thread(r, "ThreadPoolUtil #" + mCount.getAndIncrement())
            }

        }

        private val sPoolWorkQueue = LinkedBlockingQueue<Runnable>(10)

        val THREAD_POOL_EXECUTOR: Executor = ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE.toLong(),
            TimeUnit.SECONDS,
            sPoolWorkQueue,
            sThreadFactory,
            ThreadPoolExecutor.DiscardOldestPolicy()
        )

        fun executor(threadName: String, body: () -> Unit) {
            THREAD_POOL_EXECUTOR.execute {
                Thread.currentThread().name = threadName
                if (AppEnv.DEBUG) {
                    Log.i(TAG, "thread name = ${Thread.currentThread().name}")
                }
                body()
            }
        }

        fun rxjavaExecutor(threadName: String, body: () -> Unit) {
            var disposable: Disposable? = null
            disposable = Flowable.fromArray(0).subscribeOn(Schedulers.io()).subscribe{
                Thread.currentThread().name = threadName
                if (DEBUG){
                    Log.i(TAG,"rxjava thread name = $threadName")
                }
                body()
                if (DEBUG){
                    Log.i(TAG,"before is dispose ${disposable?.isDisposed}")
                }
                disposable?.dispose()
                if (DEBUG){
                    Log.i(TAG,"after is dispose ${disposable?.isDisposed}")
                }
            }
        }
    }
}