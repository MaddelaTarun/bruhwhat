package com.manekelsa.app.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manekelsa.app.model.WorkerSkill
import com.manekelsa.app.ui.theme.DeepSaffron

/**
 * SkillFilterChips — horizontal scrollable row of skill filter chips.
 * Includes an "ಎಲ್ಲಾ" (All) chip to clear the filter.
 */
@Composable
fun SkillFilterChips(
    selectedSkill: WorkerSkill?,
    onSkillSelected: (WorkerSkill?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // "All" chip
        FilterChip(
            selected = selectedSkill == null,
            onClick = { onSkillSelected(null) },
            label = {
                Text(
                    text = "ಎಲ್ಲಾ",
                    fontSize = 15.sp
                )
            },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = DeepSaffron,
                selectedLabelColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp)
        )

        // One chip per skill
        WorkerSkill.entries.forEach { skill ->
            val (icon, color) = skillIconData(skill)
            FilterChip(
                selected = selectedSkill == skill,
                onClick = { onSkillSelected(if (selectedSkill == skill) null else skill) },
                label = {
                    Text(
                        text = skill.kannada,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (selectedSkill == skill) Color.White else color,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = color,
                    selectedLabelColor = Color.White,
                    selectedLeadingIconColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}
