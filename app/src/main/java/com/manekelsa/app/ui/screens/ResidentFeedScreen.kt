package com.manekelsa.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manekelsa.app.R
import com.manekelsa.app.ui.components.SkillFilterChips
import com.manekelsa.app.ui.components.WorkerCard
import com.manekelsa.app.ui.theme.DeepSaffron
import com.manekelsa.app.viewmodel.WorkerFeedViewModel

/**
 * ResidentFeedScreen — the main discovery screen for residents.
 * Shows available workers sorted by proximity with skill filters.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResidentFeedScreen(
    viewModel: WorkerFeedViewModel = viewModel()
) {
    val context = LocalContext.current
    val workers by viewModel.workers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessageRes by viewModel.errorMessageRes.collectAsState()
    val selectedSkill by viewModel.selectedSkill.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val thumbsUpFeedback by viewModel.thumbsUpFeedback.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // Show error in snackbar
    val errorMessage = errorMessageRes?.let { context.getString(it) }
    LaunchedEffect(errorMessageRes) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.feed_title),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = stringResource(R.string.feed_subtitle),
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepSaffron
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // ── Search bar ────────────────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_hint),
                        tint = DeepSaffron
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            )

            // ── Skill filter chips ────────────────────────────────────────────
            SkillFilterChips(
                selectedSkill = selectedSkill,
                onSkillSelected = { viewModel.setSkillFilter(it) }
            )

            // ── Worker count label ────────────────────────────────────────────
            if (!isLoading) {
                Text(
                    text = stringResource(R.string.workers_available_count, workers.size),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            // ── Content ───────────────────────────────────────────────────────
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = DeepSaffron,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                workers.isEmpty() -> {
                    EmptyFeedMessage()
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = workers,
                            key = { it.id }
                        ) { worker ->
                            WorkerCard(
                                worker = worker,
                                onCallClick = { phone ->
                                    // ACTION_DIAL — shows dialer without auto-calling
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:$phone")
                                    }
                                    context.startActivity(intent)
                                },
                                onThumbsUp = { viewModel.giveThumbsUp(it) },
                                isThumbsUpAnimating = worker.id in thumbsUpFeedback
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyFeedMessage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "😔",
                fontSize = 56.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_workers_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.no_workers_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
