package io.rndev.paparcar.domain.service

import io.rndev.paparcar.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

interface ActivityRecognitionService {
    fun getActivityUpdates(): Flow<ActivityType>
}
