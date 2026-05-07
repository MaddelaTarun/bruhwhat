package com.manekelsa.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manekelsa.app.R
import com.manekelsa.app.model.WorkerSkill
import com.manekelsa.app.ui.components.AvailabilityToggle
import com.manekelsa.app.ui.components.SkillIcon
import com.manekelsa.app.ui.components.getSkillName
import com.manekelsa.app.ui.theme.DeepSaffron
import com.manekelsa.app.viewmodel.WorkerProfileViewModel

/**
 * WorkerProfileScreen — where a worker registers or edits their profile
 * and toggles their daily availability.
 *
 * @param workerId null = new registration, non-null = edit existing profile
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerProfileScreen(
    workerId: String? = null,
    viewModel: WorkerProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val isSaving by viewModel.isSaving.collectAsState()
    val saveSuccess by viewModel.saveSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val nameInput by viewModel.nameInput.collectAsState()
    val phoneInput by viewModel.phoneInput.collectAsState()
    val areaInput by viewModel.areaInput.collectAsState()
    val dailyRateInput by viewModel.dailyRateInput.collectAsState()
    val selectedSkill by viewModel.selectedSkill.collectAsState()
    val isAvailable by viewModel.isAvailable.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var skillDropdownExpanded by remember { mutableStateOf(false) }

    // Load existing profile if editing
    LaunchedEffect(workerId) {
        workerId?.let { viewModel.loadProfile(it) }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(saveSuccess) {
        if (saveSuccess == true) {
            snackbarHostState.showSnackbar(context.getString(R.string.save_success))
            viewModel.clearSaveSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(if (workerId == null) R.string.profile_title_new else R.string.profile_title_edit),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepSaffron)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Availability toggle (only for existing workers) ───────────────
            if (workerId != null) {
                AvailabilityToggle(
                    isAvailable = isAvailable,
                    onToggle = { viewModel.toggleAvailability(workerId) }
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // ── Section header ────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.section_my_info),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // ── Name field ────────────────────────────────────────────────────
            OutlinedTextField(
                value = nameInput,
                onValueChange = { viewModel.nameInput.value = it },
                label = { Text(stringResource(R.string.field_name)) },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = DeepSaffron)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Phone field ───────────────────────────────────────────────────
            OutlinedTextField(
                value = phoneInput,
                onValueChange = { viewModel.phoneInput.value = it },
                label = { Text(stringResource(R.string.field_phone)) },
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null, tint = DeepSaffron)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Area field ────────────────────────────────────────────────────
            OutlinedTextField(
                value = areaInput,
                onValueChange = { viewModel.areaInput.value = it },
                label = { Text(stringResource(R.string.field_area)) },
                leadingIcon = {
                    Icon(Icons.Filled.Place, contentDescription = null, tint = DeepSaffron)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Daily rate field ──────────────────────────────────────────────
            OutlinedTextField(
                value = dailyRateInput,
                onValueChange = { viewModel.dailyRateInput.value = it },
                label = { Text(stringResource(R.string.field_daily_rate)) },
                leadingIcon = {
                    Icon(Icons.Filled.Payments, contentDescription = null, tint = DeepSaffron)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ── Skill dropdown ────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.field_skill),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            ExposedDropdownMenuBox(
                expanded = skillDropdownExpanded,
                onExpandedChange = { skillDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = getSkillName(context, selectedSkill),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.skill_select_hint)) },
                    leadingIcon = {
                        SkillIcon(skill = selectedSkill, size = 32.dp)
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = skillDropdownExpanded) },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = skillDropdownExpanded,
                    onDismissRequest = { skillDropdownExpanded = false }
                ) {
                    WorkerSkill.entries.forEach { skill ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    SkillIcon(skill = skill, size = 36.dp)
                                    Text(
                                        text = getSkillName(context, skill),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            },
                            onClick = {
                                viewModel.selectedSkill.value = skill
                                skillDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Save button ───────────────────────────────────────────────────
            Button(
                onClick = { viewModel.saveProfile(workerId) },
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = DeepSaffron),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = stringResource(if (workerId == null) R.string.button_register else R.string.button_save),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
