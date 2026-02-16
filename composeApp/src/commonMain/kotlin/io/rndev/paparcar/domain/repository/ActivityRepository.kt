package io.rndev.paparcar.domain.repository

import io.rndev.paparcar.domain.model.ActivityType
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivityUpdates(): Flow<ActivityType>
}
