package com.manekelsa.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manekelsa.app.R
import com.manekelsa.app.ui.theme.AvailableGreen
import com.manekelsa.app.ui.theme.UnavailableGray

/**
 * AvailabilityToggle — the hero component on the worker's profile screen.
 * Large, high-contrast, with localized labels.
 */
@Composable
fun AvailabilityToggle(
    isAvailable: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor by animateColorAsState(
        targetValue = if (isAvailable) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
        animationSpec = tween(300),
        label = "toggleBg"
    )
    val textColor by animateColorAsState(
        targetValue = if (isAvailable) AvailableGreen else UnavailableGray,
        animationSpec = tween(300),
        label = "toggleText"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isAvailable) Icons.Filled.CheckCircle else Icons.Filled.Cancel,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(if (isAvailable) R.string.available_today else R.string.unavailable_today),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(if (isAvailable) R.string.available_description else R.string.unavailable_description),
                    fontSize = 13.sp,
                    color = textColor.copy(alpha = 0.75f)
                )
            }
        }

        Switch(
            checked = isAvailable,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AvailableGreen,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = UnavailableGray
            ),
            modifier = Modifier.size(width = 64.dp, height = 36.dp)
        )
    }
}
