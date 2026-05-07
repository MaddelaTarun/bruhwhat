package com.manekelsa.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manekelsa.app.model.WorkerSkill

/**
 * SkillIcon — circular icon badge representing a worker's skill.
 * Uses high-contrast colors and large touch targets for accessibility.
 */
@Composable
fun SkillIcon(
    skill: WorkerSkill,
    size: Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    val (icon, bgColor) = skillIconData(skill)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .background(bgColor, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = skill.kannada,
            tint = Color.White,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}

fun skillIconData(skill: WorkerSkill): Pair<ImageVector, Color> = when (skill) {
    WorkerSkill.CLEANING -> Icons.Filled.CleaningServices to Color(0xFF1565C0)   // Blue
    WorkerSkill.GARDENING -> Icons.Filled.Agriculture to Color(0xFF2E7D32)        // Green
    WorkerSkill.COOKING -> Icons.Filled.Restaurant to Color(0xFFE65100)           // Orange
    WorkerSkill.WASHING -> Icons.Filled.LocalLaundryService to Color(0xFF6A1B9A)  // Purple
    WorkerSkill.SECURITY -> Icons.Filled.Security to Color(0xFFB71C1C)            // Red
    WorkerSkill.DRIVING -> Icons.Filled.DirectionsCar to Color(0xFF00695C)        // Teal
    WorkerSkill.OTHER -> Icons.Filled.Work to Color(0xFF4E342E)                   // Brown
}
