package com.example.presentation.components.scaffold

import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.components.messages.snackbar.AppSnackbarPosition
import com.example.presentation.components.messages.snackbar.AppSnackbarScreenContent

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    snackbarPosition: AppSnackbarPosition = AppSnackbarPosition.BOTTOM,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    bottomPadding: Dp = 0.dp,
    screenContent: @Composable () -> Unit,
    snackbarContent: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = topBar,
        bottomBar = bottomBar,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        snackbarHost = { scaffoldState.snackbarHostState }
    ) {
        val paddingValues = it

        AppSnackbarScreenContent(
            screenContent = screenContent,
            snackbarPosition = snackbarPosition,
            bottomPadding = bottomPadding,
            snackbarContent = snackbarContent
        )
    }
}