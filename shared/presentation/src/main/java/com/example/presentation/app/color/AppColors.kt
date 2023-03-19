package com.example.presentation.app.color

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

data class AppColors(
    val material: Colors,
    val primaryTextWarning: Color,
    val onFeedbackDangerPrimary: Color,
    val onFeedbackWarningPrimary: Color,
    val onFeedbackSuccessPrimary: Color,
    val onSurfacePrimary: Color,
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val background: Color get() = material.background
    val onBackground: Color get() = material.onBackground
    val error: Color get() = material.error
    val onError: Color get() = material.onError
}
