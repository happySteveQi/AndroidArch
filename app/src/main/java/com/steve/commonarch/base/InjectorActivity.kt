package com.steve.commonarch.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.steve.commonarch.di.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

open class InjectorActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    lateinit var viewModelFactory: ViewModelProvider.Factory

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    @Inject
    lateinit var dispatchingAndroidInject: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInject

    inline fun <reified T : ViewModel> lazyViewModel(): Lazy<T> {
        return lazy {
            ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
        }
    }

    @Inject
    fun injectViewModelFactory(viewModelFactory: ViewModelFactory) {
        if (this::viewModelFactory.isInitialized) return
        this.viewModelFactory = viewModelFactory
    }
}