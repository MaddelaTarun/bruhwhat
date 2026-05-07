# ಮನೆ-ಕೆಲಸ (Mane-Kelsa)
### Digital Naka — Hyper-Local Work Directory

An Android app connecting domestic workers and gardeners in small towns with nearby residents for short-term tasks.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Backend | Firebase Realtime Database |
| Image Loading | Coil |
| Architecture | MVVM + Clean Architecture |
| Localization | 100% Kannada (ಕನ್ನಡ) |

---

## Project Structure

```
app/src/main/java/com/manekelsa/app/
├── MainActivity.kt                    # Single-activity entry point
├── model/
│   └── WorkerProfile.kt               # Core data model + WorkerSkill enum
├── data/
│   └── FirebaseRepository.kt          # All Firebase CRUD + real-time streams
├── viewmodel/
│   ├── WorkerFeedViewModel.kt         # Resident feed: filter, sort, thumbs-up
│   └── WorkerProfileViewModel.kt      # Worker profile: save, availability toggle
├── navigation/
│   └── NavGraph.kt                    # Bottom nav (3 tabs) + NavHost
└── ui/
    ├── theme/
    │   ├── Theme.kt                   # High-contrast saffron/green color scheme
    │   └── Typography.kt             # Large Kannada-friendly text sizes
    ├── components/
    │   ├── WorkerCard.kt              # List item: photo, skill, call button, thumbs-up
    │   ├── SkillIcon.kt               # Circular colored skill icon badge
    │   ├── SkillFilterChips.kt        # Horizontal scrollable skill filter row
    │   └── AvailabilityToggle.kt      # Large animated availability switch
    └── screens/
        ├── ResidentFeedScreen.kt      # Main discovery feed for residents
        ├── WorkerProfileScreen.kt     # Worker registration + profile edit
        └── AboutScreen.kt            # App explanation with icon cards
```

---

## Setup Instructions

### 1. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a project named **mane-kelsa**
3. Add an Android app with package name: `com.manekelsa.app`
4. Download `google-services.json` and replace `app/google-services.json`
5. Enable **Realtime Database** in Firebase Console
6. Import `firebase/database.rules.json` as your security rules

### 2. Build & Run

```bash
./gradlew assembleDebug
```

Or open in Android Studio and run on a device/emulator (API 24+).

### 3. Seed Mock Data (Dev Only)

In `FirebaseRepository`, call `seedMockData()` once from a debug build to populate sample workers.

---

## Key Features

### For Workers
- **Profile Registration**: Name, skill, phone, area, daily rate
- **Availability Toggle**: Large switch that instantly syncs to Firebase
- Real-time status visible to all residents immediately

### For Residents
- **Live Feed**: Only shows workers with `isAvailable == true`
- **Skill Filters**: Tap chips to filter by Cleaning, Gardening, Cooking, etc.
- **Proximity Sort**: Workers sorted by distance (Haversine formula)
- **One-tap Call**: `ACTION_DIAL` intent — no auto-call, shows dialer
- **Thumbs Up**: Trust rating with animated feedback

### Accessibility (Semi-Literate Users)
- All text in **Kannada**
- **Large buttons** (min 48dp touch targets)
- **Icon-first** design — skill icons, availability badges
- **High-contrast** colors: saffron primary, green for available
- **Large typography** (16–22sp body text)

---

## Firebase Data Structure

```json
{
  "workers": {
    "-workerId123": {
      "id": "-workerId123",
      "name": "ಸುಮಾ ರೆಡ್ಡಿ",
      "skill": "CLEANING",
      "phoneNumber": "9876543210",
      "area": "ಗಾಂಧಿ ನಗರ",
      "dailyRate": 400,
      "photoUrl": "",
      "isAvailable": true,
      "thumbsUp": 12,
      "latitude": 13.0827,
      "longitude": 77.5877
    }
  }
}
```

---

## Architecture Notes

- **Repository pattern**: `FirebaseRepository` exposes Kotlin `Flow` via `callbackFlow`
- **ViewModels** collect flows in `viewModelScope` — no memory leaks
- **Optimistic UI**: Thumbs-up animates immediately, reverts on Firebase error
- **Availability toggle** updates a single boolean field — minimal bandwidth on low-end devices
- **Haversine distance** sorts workers by proximity without requiring GPS permission (uses mock/stored coordinates)
