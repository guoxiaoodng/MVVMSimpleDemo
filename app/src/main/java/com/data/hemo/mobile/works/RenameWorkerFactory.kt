package com.data.hemo.mobile.works

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/2/15
 *@time: 14:20
 *@description：
 */
class RenameWorkerFactory : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        return when(workerClassName) {
            "RxCleanupWorker" -> CleanupWorker(appContext, workerParameters)
            else -> null
        }
    }
}