package com.example.presentation.components.bottomsheet.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.app.AppTheme
import com.example.presentation.components.iconbuttons.AppCloseIconButton
import com.example.presentation.components.spacing.AppSpacing

@Composable
fun AppBottomSheetDialogContainerLayout(
    modifier: Modifier = Modifier,
    onClosePressed: () -> Unit = {},
    withTopDivider: Boolean = true,
    withCloseIcon: Boolean = false,
    closeIconDescription: String? = null,
    closeButtonColor: Color = AppTheme.colors.onBackground,
    content: @Composable () -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .background(color = AppTheme.colors.background)
    ) {
        content()

        if (withTopDivider) BottomSheetTopDivider()

        if (withCloseIcon) {
            AppCloseIconButton(
                onClick = onClosePressed,
                color = closeButtonColor,
                contentDescription = closeIconDescription,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = AppSpacing.regular, end = AppSpacing.regular)
                    .size(29.dp)
            )
        }

    }
}

@Composable
private fun BottomSheetTopDivider() {
    Column(verticalArrangement = Arrangement.Center) {
        Spacer(
            modifier = Modifier
                .height(AppSpacing.mini)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Divider(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(4.dp)
                .background(shape = RoundedCornerShape(10.dp), color = AppTheme.colors.background)
                .padding(top = 16.dp)
                .width(50.dp), color = AppTheme.colors.background
        )
    }
}