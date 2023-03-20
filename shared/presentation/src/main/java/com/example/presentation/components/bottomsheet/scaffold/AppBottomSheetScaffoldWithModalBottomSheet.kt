package com.example.presentation.components.bottomsheet.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.presentation.components.bottomsheet.BottomSheetShapeTopRounded
import com.example.presentation.components.messages.snackbar.AppSnackbarPosition

@ExperimentalMaterialApi
@Composable
fun AppBottomSheetScaffoldWithModalBottomSheet(
    modifier: Modifier = Modifier,
    snackbarBottomPadding: Dp = 0.dp,
    modalSheetState: ModalBottomSheetState,
    modalSheetContent: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {},
    scaffoldState: BottomSheetScaffoldState,
    withContainerLayout: Boolean = true,
    onCloseContainer: () -> Unit = {},
    keepSheetClosedAtStart: Boolean = true,
    snackbarPosition: AppSnackbarPosition = AppSnackbarPosition.BOTTOM,
    sheetPeekHeight: Dp = 0.dp,
    floatingActionButton: (@Composable () -> Unit)? = null,
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    bottomSheetContent: @Composable () -> Unit,
    screenContent: @Composable () -> Unit,
    snackbarContent: @Composable () -> Unit,
) {
    ModalBottomSheetLayout(sheetState = modalSheetState, sheetShape = BottomSheetShapeTopRounded, sheetContent = {
        Box(modifier = Modifier.defaultMinSize(minHeight = 1.dp)) { //This hack prevents java.lang.IllegalArgumentException: The initial value must have an associated anchor.
            modalSheetContent()
        }
    }, content = {
        AppBottomSheetScaffold(
            modifier = modifier,
            bottomPadding = snackbarBottomPadding,
            topBar = topBar,
            withContainerLayout = withContainerLayout,
            onCloseContainer = onCloseContainer,
            keepSheetClosedAtStart = keepSheetClosedAtStart,
            snackbarPosition = snackbarPosition,
            sheetPeekHeight = sheetPeekHeight,
            floatingActionButton = floatingActionButton,
            floatingActionButtonPosition = floatingActionButtonPosition,
            snackbarContent = snackbarContent,
            scaffoldState = scaffoldState,
            bottomSheetContent = bottomSheetContent,
            screenContent = screenContent
        )
    })
}