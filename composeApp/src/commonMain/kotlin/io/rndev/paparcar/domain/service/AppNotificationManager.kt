package io.rndev.paparcar.domain.service

/**
 * Manages the display of notifications throughout the application.
 * The contract is defined in commonMain, while the implementation is platform-specific.
 */
interface AppNotificationManager {

    fun showGpsTrackingInProgressNotification()

    fun showGpsTrackingSuccessNotification(latitude: Double, longitude: Double)

    fun showGpsTrackingErrorNotification()

    fun showActivityTransitionNotification(activityName: String, transitionType: String)

    fun getForegroundServiceNotificationId(): Int
}
