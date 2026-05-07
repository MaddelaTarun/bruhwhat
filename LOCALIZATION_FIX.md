# Localization Fix - Remaining Hardcoded Text

## Files That Still Need Updating

### 1. AvailabilityToggle.kt
**Lines 76-82**: Replace hardcoded Kannada with `stringResource()`

```kotlin
// BEFORE:
text = if (isAvailable) "ಇಂದು ಲಭ್ಯ" else "ಇಂದು ಲಭ್ಯವಿಲ್ಲ"
text = if (isAvailable) "ನಿವಾಸಿಗಳು ನಿಮ್ಮನ್ನು ನೋಡಬಹುದು" else "ನೀವು ಪಟ್ಟಿಯಲ್ಲಿ ಕಾಣಿಸುವುದಿಲ್ಲ"

// AFTER:
import androidx.compose.ui.res.stringResource
import com.manekelsa.app.R

text = stringResource(if (isAvailable) R.string.available_today else R.string.unavailable_today)
text = stringResource(if (isAvailable) R.string.available_description else R.string.unavailable_description)
```

### 2. AboutScreen.kt
**Multiple lines**: Replace all hardcoded Kannada

```kotlin
// Add imports:
import androidx.compose.ui.res.stringResource
import com.manekelsa.app.R

// Replace:
text = "ಅಪ್ಲಿಕೇಶನ್ ಬಗ್ಗೆ"
// With:
text = stringResource(R.string.about_title)

// Replace:
text = "ಮನೆ-ಕೆಲಸ"
// With:
text = stringResource(R.string.app_name)

// Replace:
text = "ಡಿಜಿಟಲ್ ನಾಕ"
// With:
text = stringResource(R.string.app_tagline)

// Replace:
text = "ಈ ಅಪ್ಲಿಕೇಶನ್ ಮನೆ ಕೆಲಸಗಾರರು ಮತ್ತು ನಿವಾಸಿಗಳನ್ನು ಸಂಪರ್ಕಿಸುತ್ತದೆ."
// With:
text = stringResource(R.string.app_description)

// Replace:
text = "ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ?"
// With:
text = stringResource(R.string.how_it_works)

// Replace HowItWorksCard calls:
HowItWorksCard(
    icon = Icons.Filled.CleaningServices,
    iconColor = Color(0xFF1565C0),
    title = stringResource(R.string.about_workers_title),
    description = stringResource(R.string.about_workers_desc)
)

HowItWorksCard(
    icon = Icons.Filled.ToggleOn,
    iconColor = ForestGreen,
    title = stringResource(R.string.about_toggle_title),
    description = stringResource(R.string.about_toggle_desc)
)

HowItWorksCard(
    icon = Icons.Filled.Groups,
    iconColor = DeepSaffron,
    title = stringResource(R.string.about_residents_title),
    description = stringResource(R.string.about_residents_desc)
)

HowItWorksCard(
    icon = Icons.Filled.PhoneEnabled,
    iconColor = Color(0xFF2E7D32),
    title = stringResource(R.string.about_call_title),
    description = stringResource(R.string.about_call_desc)
)

// Replace:
text = "ಆವೃತ್ತಿ 1.0"
// With:
text = stringResource(R.string.version)
```

### 3. WorkerProfileScreen.kt
**Multiple lines**: Replace all hardcoded Kannada

```kotlin
// Add imports:
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import com.manekelsa.app.R
import com.manekelsa.app.ui.components.getSkillName

// Replace title:
text = if (workerId == null) stringResource(R.string.profile_title_new) else stringResource(R.string.profile_title_edit)

// Replace section header:
text = stringResource(R.string.section_my_info)

// Replace field labels:
label = { Text(stringResource(R.string.field_name)) }
label = { Text(stringResource(R.string.field_phone)) }
label = { Text(stringResource(R.string.field_area)) }
label = { Text(stringResource(R.string.field_daily_rate)) }

// Replace skill section:
text = stringResource(R.string.field_skill)

// Replace skill dropdown:
val context = LocalContext.current
value = getSkillName(context, selectedSkill)
label = { Text(stringResource(R.string.skill_select_hint)) }

// In dropdown menu:
Text(
    text = getSkillName(context, skill),
    style = MaterialTheme.typography.bodyLarge
)

// Replace save button:
text = if (workerId == null) stringResource(R.string.button_register) else stringResource(R.string.button_save)

// Replace success message:
snackbarHostState.showSnackbar(stringResource(R.string.save_success))
```

### 4. WorkerProfileViewModel.kt
**Error messages**: Replace hardcoded Kannada

```kotlin
// In saveProfile():
_errorMessage.value = "ದಯವಿಟ್ಟು ಎಲ್ಲಾ ಮಾಹಿತಿ ತುಂಬಿರಿ"
// This needs context to use stringResource
// Solution: Pass context to ViewModel or use resource IDs

// Better approach: Store resource ID instead of string
private val _errorMessageRes = MutableStateFlow<Int?>(null)
val errorMessageRes: StateFlow<Int?> = _errorMessageRes

// Then in UI:
val errorMessageRes by viewModel.errorMessageRes.collectAsState()
errorMessageRes?.let {
    LaunchedEffect(it) {
        snackbarHostState.showSnackbar(stringResource(it))
        viewModel.clearError()
    }
}
```

---

## Quick Fix Script

Since these are straightforward replacements, here's the pattern:

1. **Add imports** to each file:
```kotlin
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import com.manekelsa.app.R
```

2. **Replace hardcoded strings** with:
```kotlin
stringResource(R.string.resource_name)
```

3. **For skill names**, use the helper function:
```kotlin
val context = LocalContext.current
getSkillName(context, skill)
```

---

## Testing After Fix

1. **Build app** with English selected
2. **Check all screens**:
   - [ ] Feed screen → All English
   - [ ] Profile screen → All English
   - [ ] About screen → All English
   - [ ] Availability toggle → All English

3. **Clear app data** and select Kannada
4. **Check all screens again** → All Kannada

5. **Test language switching**:
   - Clear data → Select English
   - Clear data → Select Kannada
   - Verify consistency

---

## Why Some Text is Still Hardcoded

The original requirement was "100% Kannada" so all text was hardcoded in Kannada. When we added language selection, we:

✅ Created English strings.xml (default)
✅ Created Kannada strings.xml (values-kn)
✅ Updated main screens (Feed, Navigation)
✅ Updated components (WorkerCard, SkillFilterChips)

❌ Still need to update:
- AvailabilityToggle
- AboutScreen
- WorkerProfileScreen
- Error messages in ViewModels

---

## Estimated Time to Fix

- **AvailabilityToggle**: 5 minutes
- **AboutScreen**: 10 minutes
- **WorkerProfileScreen**: 15 minutes
- **ViewModels**: 10 minutes (refactor to use resource IDs)

**Total**: ~40 minutes

---

## Alternative: Keep Some Kannada

If the target audience is primarily Kannada-speaking, you could:

1. Keep worker-facing screens (Profile) in Kannada
2. Keep resident-facing screens (Feed) multilingual
3. Add a language toggle in Settings for power users

This hybrid approach might be more practical for the actual use case.
