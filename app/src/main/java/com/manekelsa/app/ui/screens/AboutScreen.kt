package com.manekelsa.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.PhoneEnabled
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manekelsa.app.ui.theme.DeepSaffron
import com.manekelsa.app.ui.theme.ForestGreen

/**
 * AboutScreen — explains the app to new users with icon-heavy, minimal-text layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ಅಪ್ಲಿಕೇಶನ್ ಬಗ್ಗೆ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepSaffron)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // App logo / hero
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(DeepSaffron, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.CleaningServices,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "ಮನೆ-ಕೆಲಸ",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DeepSaffron
            )

            Text(
                text = "ಡಿಜಿಟಲ್ ನಾಕ",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ಈ ಅಪ್ಲಿಕೇಶನ್ ಮನೆ ಕೆಲಸಗಾರರು ಮತ್ತು ನಿವಾಸಿಗಳನ್ನು ಸಂಪರ್ಕಿಸುತ್ತದೆ.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(28.dp))

            // How it works cards
            Text(
                text = "ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            HowItWorksCard(
                icon = Icons.Filled.CleaningServices,
                iconColor = Color(0xFF1565C0),
                title = "ಕೆಲಸಗಾರರು",
                description = "ನಿಮ್ಮ ಪ್ರೊಫೈಲ್ ಮಾಡಿ ಮತ್ತು ಇಂದು ಲಭ್ಯ ಎಂದು ತೋರಿಸಿ"
            )

            Spacer(modifier = Modifier.height(10.dp))

            HowItWorksCard(
                icon = Icons.Filled.ToggleOn,
                iconColor = ForestGreen,
                title = "ಲಭ್ಯತೆ ಟಾಗಲ್",
                description = "ಪ್ರತಿ ದಿನ ಬೆಳಿಗ್ಗೆ ಸ್ವಿಚ್ ಆನ್ ಮಾಡಿ, ಕೆಲಸ ಮುಗಿದ ನಂತರ ಆಫ್ ಮಾಡಿ"
            )

            Spacer(modifier = Modifier.height(10.dp))

            HowItWorksCard(
                icon = Icons.Filled.Groups,
                iconColor = DeepSaffron,
                title = "ನಿವಾಸಿಗಳು",
                description = "ಹತ್ತಿರದ ಲಭ್ಯ ಕೆಲಸಗಾರರನ್ನು ನೋಡಿ ಮತ್ತು ಆಯ್ಕೆ ಮಾಡಿ"
            )

            Spacer(modifier = Modifier.height(10.dp))

            HowItWorksCard(
                icon = Icons.Filled.PhoneEnabled,
                iconColor = Color(0xFF2E7D32),
                title = "ನೇರ ಕರೆ",
                description = "ಒಂದು ಟ್ಯಾಪ್‌ನಲ್ಲಿ ಕೆಲಸಗಾರರಿಗೆ ಕರೆ ಮಾಡಿ"
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ಆವೃತ್ತಿ 1.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HowItWorksCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .background(iconColor.copy(alpha = 0.15f), CircleShape)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
