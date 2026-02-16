package io.rndev.paparcar.platform.model

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * A singleton event bus for permission results, allowing decoupling between the
 * Android Activity that requests permissions and the ViewModel that reacts to them.
 */
class PermissionEventSource {
    private val _events = MutableSharedFlow<PermissionEvent>()
    val events = _events.asSharedFlow()

    fun postEvent(event: PermissionEvent) {
        _events.tryEmit(event)
    }
}
