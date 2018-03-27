package me.leoyuu.mysqlclient

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

/**
 * date 2018/2/25
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */
class App:Application() {

    override fun onCreate() {
        super.onCreate()
        app = this

        UMConfigure.init(this, getString(R.string.umeng_app_key), "tencent", UMConfigure.DEVICE_TYPE_PHONE, null)
        Thread.setDefaultUncaughtExceptionHandler { _, e -> MobclickAgent.reportError(this@App, e) }


        handleLifeCircle();
    }


    private fun handleLifeCircle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {

            override fun onActivityPaused(activity: Activity?) {
                MobclickAgent.onPause(activity)
            }

            override fun onActivityResumed(activity: Activity?) {
                MobclickAgent.onResume(activity)
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
            override fun onActivityDestroyed(activity: Activity?) {}
            override fun onActivityStarted(activity: Activity?) {}
            override fun onActivityStopped(activity: Activity?) {}
        })
    }

    companion object {
        lateinit var app:App
            private set
    }
}