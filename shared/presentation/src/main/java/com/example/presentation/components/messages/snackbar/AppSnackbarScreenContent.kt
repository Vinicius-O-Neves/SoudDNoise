package com.example.presentation.components.messages.snackbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun AppSnackbarScreenContent(
    screenContent: @Composable () -> Unit,
    snackbarPosition: AppSnackbarPosition,
    bottomPadding: Dp,
    snackbarContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //Screen
        screenContent()

        if (snackbarPosition == AppSnackbarPosition.BOTTOM) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = bottomPadding)
            ) {
                snackbarContent()
            }
        } else {
            snackbarContent()
        }

    }
}