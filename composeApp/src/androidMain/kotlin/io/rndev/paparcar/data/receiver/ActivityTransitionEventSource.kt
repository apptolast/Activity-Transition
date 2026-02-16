package io.rndev.paparcar.data.receiver

import com.google.android.gms.location.ActivityTransitionEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ActivityTransitionEventSource {
    private val _events = MutableSharedFlow<ActivityTransitionEvent>(replay = 1)
    val events = _events.asSharedFlow()

    internal fun postEvent(event: ActivityTransitionEvent) {
        _events.tryEmit(event)
    }
}
