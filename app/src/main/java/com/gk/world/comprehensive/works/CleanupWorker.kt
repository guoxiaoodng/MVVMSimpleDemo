package com.gk.world.comprehensive.works

import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 *版权 ： GXD
 *@author: gxd
 *@date: 2022/2/15
 *@time: 14:21
 *@description：
 */
class CleanupWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val targetDirectory = File(applicationContext.filesDir, Constants.OUTPUT_PATH)

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                cleanupDirectory()
                Result.success()
            } catch (exception: Exception) {
                Log.e(TAG, "Error cleaning up", exception)
                Result.failure()
            }
        }
    }

    /** Removes pngs from the app's files directory */
    private fun cleanupDirectory() {
        targetDirectory.apply {
            if (exists()) {
                listFiles()?.forEach { file ->
                    if (file.name.endsWith(".png")) {
                        val deleted = file.delete()
                        Log.i(TAG, "Deleted ${file.name} - $deleted")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "CleanupWorker"
    }
}