package com.example.presentation.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface ObservableLifecycleOwner : LifecycleOwner {

    fun <T> collectFlow(
        flow: Flow<T>, lifecycleState: Lifecycle.State = Lifecycle.State.STARTED, onCollect: (T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(lifecycleState) {
                flow.collect {
                    onCollect(it)
                }
            }
        }
    }

}