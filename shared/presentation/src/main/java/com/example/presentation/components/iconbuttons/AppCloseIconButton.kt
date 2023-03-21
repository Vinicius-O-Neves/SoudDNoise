package com.example.presentation.components.iconbuttons

import androidx.compose.foundation.Image
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.presentation.app.AppTheme
import com.example.shared.presentation.R

@Composable
fun AppCloseIconButton(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.onBackground,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    IconButton(modifier = modifier, onClick = { onClick() }) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_x),
            contentDescription = contentDescription ?: "",
            colorFilter = ColorFilter.tint(color, BlendMode.SrcIn)
        )
    }
}