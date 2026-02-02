package io.rndev.paparcar.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DebugLogger {
    private val _logs = MutableStateFlow<List<String>>(emptyList())
    val logs = _logs.asStateFlow()

    fun log(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault()).format(Date())
        val newLogs = _logs.value + "[$timestamp] $message"
        _logs.value = newLogs.takeLast(100) // Keep the list from growing indefinitely
    }
}
