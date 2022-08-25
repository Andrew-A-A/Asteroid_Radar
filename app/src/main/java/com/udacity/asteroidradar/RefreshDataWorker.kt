package com.udacity.asteroidradar

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Database.getDatabase
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RefreshDataWorker(appContext: Context,params: WorkerParameters):CoroutineWorker(appContext,params) {
    companion object{
        const val WORK_NAME="RefreshDataWorker"
    }

    private val current: LocalDateTime = LocalDateTime.now()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
    private val startDate: String =current.format(formatter)
    override suspend fun doWork(): Result {
        val database= getDatabase(applicationContext)
        val repository=DataRepository(database)
        return try {
            repository.refreshAsteroids(startDate)
            Result.success()
        }catch (e: HttpException){
            Result.retry()
        }
    }
}