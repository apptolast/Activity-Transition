package io.rndev.paparcar.presentation.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, I, E> {

    private val viewModelJob = SupervisorJob()
    protected val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _state: MutableStateFlow<S> by lazy { MutableStateFlow(initState()) }
    val state = _state.asStateFlow()

    private val _effect: MutableSharedFlow<E> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    abstract fun initState(): S

    abstract fun handleIntent(intent: I)

    protected fun updateState(handler: S.() -> S) {
        _state.update(handler)
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effect.emit(effect) }
    }

    fun onClear() {
        viewModelScope.cancel()
    }
}
