package io.rndev.paparcar.data.repository

import io.rndev.paparcar.data.source.ActivityRecognitionDataSource
import io.rndev.paparcar.domain.model.ActivityType
import io.rndev.paparcar.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class ActivityRepositoryImpl(
    private val dataSource: ActivityRecognitionDataSource
) : ActivityRepository {

    override fun getActivityUpdates(): Flow<ActivityType> {
        return dataSource.getActivityUpdates()
    }
}
