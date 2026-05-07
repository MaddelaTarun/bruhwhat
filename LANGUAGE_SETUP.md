# Language Selection Feature

## What Changed

The app now supports **multi-language** functionality with a language selection screen on first launch.

### Supported Languages
- **English** (default)
- **ಕನ್ನಡ Kannada**
- **हिंदी Hindi** (strings ready, needs translation)

---

## How It Works

### First Launch
1. App opens to **Language Selection Screen** (in English)
2. User chooses their preferred language
3. Clicks "Continue"
4. App remembers the choice and shows main feed

### Subsequent Launches
- App opens directly to the main feed in the selected language
- Language preference is saved in SharedPreferences

---

## File Structure

### New Files
```
app/src/main/java/com/manekelsa/app/
├── data/PreferencesManager.kt          # Stores language preference
└── ui/screens/LanguageSelectionScreen.kt  # Language picker UI

app/src/main/res/
├── values/strings.xml                  # English strings (default)
└── values-kn/strings.xml              # Kannada strings
```

### Modified Files
- `MainActivity.kt` — Handles locale changes and first launch detection
- `NavGraph.kt` — Added language selection route
- `ResidentFeedScreen.kt` — Uses string resources instead of hardcoded text

---

## Adding More Languages

### Step 1: Create Resource File
```bash
# For Hindi
mkdir -p app/src/main/res/values-hi
touch app/src/main/res/values-hi/strings.xml
```

### Step 2: Copy and Translate
Copy `app/src/main/res/values/strings.xml` and translate all values.

### Step 3: Update Language List
In `LanguageSelectionScreen.kt`, the languages list already includes Hindi:
```kotlin
Language("hi", R.string.language_hindi, "हिंदी")
```

---

## Testing Different Languages

### Method 1: Reset First Launch
```kotlin
// In MainActivity.onCreate(), temporarily add:
prefsManager.isFirstLaunch = true
```

### Method 2: Change Device Language
Settings → System → Languages → Add Kannada

### Method 3: Clear App Data
Settings → Apps → Mane-Kelsa → Storage → Clear Data

---

## How Locale Switching Works

```kotlin
// MainActivity.setAppLocale()
val locale = Locale(languageCode)  // "en", "kn", "hi"
Locale.setDefault(locale)
config.setLocale(locale)
recreate()  // Restarts activity with new locale
```

Android automatically loads the correct `strings.xml`:
- `values/strings.xml` → English (default)
- `values-kn/strings.xml` → Kannada
- `values-hi/strings.xml` → Hindi (when added)

---

## String Resource Usage

### Before (Hardcoded)
```kotlin
Text(text = "ಮನೆ-ಕೆಲಸ")
```

### After (Localized)
```kotlin
Text(text = stringResource(R.string.feed_title))
```

Android picks the right translation based on the current locale.

---

## User Flow

```
┌─────────────────────────┐
│   First Launch?         │
└───────┬─────────────────┘
        │
    ┌───▼───┐
    │  Yes  │
    └───┬───┘
        │
        ▼
┌─────────────────────────┐
│ Language Selection      │
│ ┌─────────────────────┐ │
│ │ English             │ │
│ │ ಕನ್ನಡ               │ │
│ │ हिंदी               │ │
│ └─────────────────────┘ │
│      [Continue]         │
└───────┬─────────────────┘
        │
        ▼
┌─────────────────────────┐
│ Save preference         │
│ Set locale              │
│ Mark: not first launch  │
└───────┬─────────────────┘
        │
        ▼
┌─────────────────────────┐
│ Main Feed (localized)   │
└─────────────────────────┘
```

---

## Future Enhancements

1. **Settings Screen**: Add language switcher in About/Settings
2. **More Languages**: Tamil, Telugu, Marathi, etc.
3. **RTL Support**: For Urdu/Arabic if needed
4. **Voice Input**: For semi-literate users
5. **Icon-only Mode**: Minimal text, maximum icons

---

## Notes

- Language preference persists across app restarts
- Changing language requires activity recreation (brief flash)
- All UI text should use `stringResource()` for proper localization
- Worker skill names in the database remain in Kannada (data layer)
- UI displays them based on the selected language (presentation layer)
