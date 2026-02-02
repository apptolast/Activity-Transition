package io.rndev.paparcar.data.source

import io.rndev.paparcar.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

interface ActivityRecognitionDataSource {
    fun getActivityUpdates(): Flow<ActivityType>
}
