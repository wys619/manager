package cn.woyeshi.manager

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cn.woyeshi.base.utils.FrescoUtils
import cn.woyeshi.base.utils.MemoryTrimmable

class ManagerApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        //初始化图片加载框架
        FrescoUtils.initFresco(applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        MemoryTrimmable.onLowMemory()
    }


}