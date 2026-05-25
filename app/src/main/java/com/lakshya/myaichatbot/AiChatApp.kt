package com.lakshya.myaichatbot

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class AiChatApp: Application(), Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    private lateinit var appOpenAdManager: AppOpenAdManager
    private var currentActivity: Activity? = null

    override fun onCreate() {
        super<Application>.onCreate()
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
    }

    // Inner class handling the specific AdMob mechanics
    private inner class AppOpenAdManager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        // Google's official Test Ad Unit ID for App Open Ads
        private val adUnitId = "ca-app-pub-3940256099942544/9257391924"

        fun loadAd() {
            if (isLoadingAd || isAdAvailable()) return
            isLoadingAd = true

            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                this@AiChatApp,
                adUnitId,
                request,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                    }
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                    }
                }
            )
        }

        private fun isAdAvailable(): Boolean = appOpenAd != null

        fun showAdIfAvailable(activity: Activity) {
            if (isAdAvailable()) {
                appOpenAd?.show(activity)
                appOpenAd = null // Prevent duplicate displays
                loadAd() // Preload the next ad
            } else {
                loadAd()
            }
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) { currentActivity = activity }
    override fun onActivityResumed(activity: Activity) { currentActivity = activity }
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) { if (currentActivity == activity) currentActivity = null }

}