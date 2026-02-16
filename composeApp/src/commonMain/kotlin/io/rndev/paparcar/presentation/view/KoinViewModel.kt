package io.rndev.paparcar.presentation.view

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject
import io.rndev.paparcar.presentation.viewmodel.BaseViewModel

/**
 * A helper function to get a KMP-compatible ViewModel from Koin in a Composable function.
 */
@Composable
inline fun <reified T : BaseViewModel<*, *, *>> getViewModel(): T {
    return koinInject()
}
