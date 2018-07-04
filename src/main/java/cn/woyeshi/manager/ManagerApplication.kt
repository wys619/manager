package cn.woyeshi.manager

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import cn.woyeshi.base.utils.FrescoUtils
import cn.woyeshi.base.utils.MemoryTrimmable
import cn.woyeshi.entity.utils.ContextHolder
import java.lang.ref.WeakReference

class ManagerApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        ContextHolder.setApplicationContext(WeakReference(this))
        //初始化图片加载框架
        FrescoUtils.initFresco(applicationContext)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        MemoryTrimmable.onLowMemory()
    }


}