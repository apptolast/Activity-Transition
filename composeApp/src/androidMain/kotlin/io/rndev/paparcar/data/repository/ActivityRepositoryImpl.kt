package io.rndev.paparcar.data.repository

import io.rndev.paparcar.domain.model.ActivityType
import io.rndev.paparcar.domain.repository.ActivityRepository
import io.rndev.paparcar.domain.service.ActivityRecognitionService
import kotlinx.coroutines.flow.Flow

class ActivityRepositoryImpl(
    private val service: ActivityRecognitionService
) : ActivityRepository {

    override fun getActivityUpdates(): Flow<ActivityType> {
        return service.getActivityUpdates()
    }
}
