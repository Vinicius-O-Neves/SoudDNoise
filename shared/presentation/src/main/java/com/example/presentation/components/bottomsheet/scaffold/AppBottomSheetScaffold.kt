package com.example.presentation.components.bottomsheet.scaffold

import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.components.bottomsheet.BottomSheetShapeTopRounded
import com.example.presentation.components.messages.snackbar.AppSnackbarPosition
import com.example.presentation.components.messages.snackbar.AppSnackbarScreenContent

@ExperimentalMaterialApi
@Composable
fun AppBottomSheetScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    scaffoldState: BottomSheetScaffoldState,
    withContainerLayout: Boolean = true,
    onCloseContainer: () -> Unit = {},
    keepSheetClosedAtStart: Boolean = true,
    snackbarPosition: AppSnackbarPosition = AppSnackbarPosition.BOTTOM,
    sheetPeekHeight: Dp = 0.dp,
    bottomPadding: Dp = 0.dp,
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    bottomSheetContent: @Composable () -> Unit,
    screenContent: @Composable () -> Unit,
    snackbarContent: @Composable () -> Unit,
) {
    BottomSheetScaffold(modifier = modifier,
        topBar = topBar,
        sheetPeekHeight = sheetPeekHeight,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        scaffoldState = scaffoldState,
        sheetShape = BottomSheetShapeTopRounded,
        snackbarHost = { scaffoldState.snackbarHostState },
        sheetContent = {
            if (withContainerLayout) {
                AppBottomSheetDialogContainerLayout(onClosePressed = onCloseContainer) {
                    bottomSheetContent()
                }
            } else {
                bottomSheetContent()
            }
        },
        content = {
            AppSnackbarScreenContent(
                screenContent = { screenContent() },
                snackbarPosition = snackbarPosition,
                bottomPadding = bottomPadding,
                snackbarContent = snackbarContent
            )

            if (keepSheetClosedAtStart) {
                LaunchedEffect(Unit) {
                    scaffoldState.bottomSheetState.collapse()
                }
            }
        })
}