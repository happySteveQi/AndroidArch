package com.steve.commonarch.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

open class BaseViewModel(repository: BaseRepository):ViewModel() {
    companion object{
        const val TAG = "BaseViewModel"
    }
    private val disposableList = arrayListOf<Disposable>()

    init {
        repository.disposableList = disposableList
    }
    val loading = MutableLiveData<Boolean>()

    fun showLoading(){
        loading.postValue(true)
    }
    fun hideLoading(){
        loading.postValue(false)
    }
    fun onDestroy(){
        disposableList.forEach {
            if (DEBUG){
                Log.d(TAG,"${this.javaClass.simpleName}-onDestory-$it--${it.isDisposed}")
            }
            if (!it.isDisposed){
                it.dispose()
            }
        }
    }
}