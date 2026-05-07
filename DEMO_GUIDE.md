# Mane-Kelsa Demo Guide

## Backend Setup (Firebase)

### Step 1: Create Firebase Project
1. Go to [console.firebase.google.com](https://console.firebase.google.com)
2. Click "Add project"
3. Name: `mane-kelsa-demo`
4. Disable Google Analytics (optional)
5. Click "Create Project"

### Step 2: Add Android App
1. Click the Android icon
2. Package name: `com.manekelsa.app` (MUST match exactly)
3. App nickname: `Mane-Kelsa`
4. Skip SHA-1 for now
5. Click "Register app"
6. **Download `google-services.json`**
7. Replace `app/google-services.json` with the downloaded file

### Step 3: Enable Realtime Database
1. In Firebase Console sidebar → Click "Realtime Database"
2. Click "Create Database"
3. Choose location: **asia-southeast1** (India) or closest
4. Start in **"Test mode"** → Enable

### Step 4: Set Database Rules
Go to "Rules" tab and paste:

```json
{
  "rules": {
    "workers": {
      ".read": true,
      "$workerId": {
        ".write": true,
        ".validate": "newData.hasChildren(['name', 'skill', 'phoneNumber', 'area', 'dailyRate', 'isAvailable'])"
      }
    }
  }
}
```

Click **"Publish"**

### Step 5: Seed Demo Data

In `MainActivity.kt`, **uncomment** this line:

```kotlin
// UNCOMMENT BELOW TO SEED TEST DATA (run once, then comment out again)
com.manekelsa.app.data.FirebaseRepository().seedMockData()
```

Run the app once, then **comment it back out** and rebuild.

---

## Demo Flow

### Part 1: Language Selection (First Launch)

**What to Show:**
1. App opens to language selection screen
2. Three options: English, ಕನ್ನಡ, हिंदी
3. Select **English** → Click "Continue"
4. App restarts in English

**Key Points:**
- Language choice is saved permanently
- Next launch goes straight to main feed
- Can be reset by clearing app data

---

### Part 2: Resident View (Finding Workers)

**What to Show:**

1. **Main Feed Screen**
   - Shows 5 mock workers (seeded data)
   - Each card shows: Photo/Icon, Name, Skill, Area, Rate, Thumbs-up count
   - Only "Available" workers appear

2. **Search Functionality**
   - Type "Gandhi" → Filters to Gandhi Nagar workers
   - Clear search → Shows all again

3. **Skill Filters**
   - Tap "Cleaning" chip → Shows only cleaning workers
   - Tap "Gardening" → Shows only gardeners
   - Tap "All" → Shows everyone

4. **Proximity Sorting**
   - Workers automatically sorted by distance
   - Uses Haversine formula on mock coordinates
   - Nearest workers appear first

5. **Call Worker**
   - Tap green "Call" button
   - Opens phone dialer (ACTION_DIAL)
   - Does NOT auto-call (safer UX)

6. **Thumbs Up**
   - Tap thumbs-up icon
   - Animates (scales up, turns green)
   - Counter increments
   - Syncs to Firebase instantly

---

### Part 3: Worker View (Profile Management)

**What to Show:**

1. **Navigate to "My Profile" Tab**
   - Bottom navigation → Second tab

2. **Register New Worker**
   - Fill in:
     - Name: "Test Worker"
     - Phone: "9876543210"
     - Area: "Test Area"
     - Daily Rate: "500"
     - Skill: Select from dropdown
   - Click "Register"
   - Success message appears

3. **Availability Toggle** (if editing existing profile)
   - Large switch at top
   - Toggle ON → "Available Today" (green)
   - Toggle OFF → "Not Available Today" (gray)
   - **Real-time sync**: Changes appear instantly in resident feed

---

### Part 4: About Screen

**What to Show:**
- Tap "About" tab
- Shows app explanation with icons
- Four cards:
  1. Workers → Create profile
  2. Availability Toggle → Daily on/off
  3. Residents → Find nearby workers
  4. Direct Call → One-tap calling

---

## Demo Script (5 Minutes)

### Minute 1: Introduction
> "Mane-Kelsa is a hyper-local work directory connecting domestic workers with residents in small towns. Think of it as a 'Digital Naka' where workers can show their daily availability."

### Minute 2: Language Selection
> "On first launch, users choose their language. We support English and Kannada, with Hindi ready. The app remembers this choice."

[Select English → Continue]

### Minute 3: Resident Experience
> "Residents see available workers sorted by proximity. They can filter by skill type, search by name or area, and call workers with one tap."

[Demo: Filter by Cleaning → Search "Gandhi" → Call a worker → Give thumbs up]

### Minute 4: Worker Experience
> "Workers create a simple profile and toggle their availability daily. When they switch to 'Available', they instantly appear in the resident feed."

[Navigate to Profile → Show availability toggle → Toggle ON/OFF]

### Minute 5: Technical Highlights
> "Built with Kotlin and Jetpack Compose for modern UI. Firebase Realtime Database ensures instant sync—when a worker toggles availability, all residents see it immediately. Designed for semi-literate users with large buttons, high contrast, and icon-heavy layout."

---

## Firebase Console Demo

### Show Real-Time Sync

1. Open Firebase Console → Realtime Database
2. In app: Toggle worker availability
3. **Watch database update live** in console
4. In app: Refresh resident feed → Worker appears/disappears

### Show Data Structure

```json
{
  "workers": {
    "-NxAbCdEfGh": {
      "id": "-NxAbCdEfGh",
      "name": "ಸುಮಾ ರೆಡ್ಡಿ",
      "skill": "CLEANING",
      "phoneNumber": "9876543210",
      "area": "ಗಾಂಧಿ ನಗರ",
      "dailyRate": 400,
      "isAvailable": true,  ← Real-time toggle
      "thumbsUp": 12,
      "latitude": 13.0827,
      "longitude": 77.5877
    }
  }
}
```

---

## Key Features to Highlight

### 1. Real-Time Availability
- Worker toggles → Instant Firebase update
- All residents see change immediately
- No polling, no refresh needed

### 2. Accessibility for Semi-Literate Users
- Large 48dp+ touch targets
- Icon-first design (broom, plant, etc.)
- High-contrast colors (saffron, green)
- Large 18-22sp text

### 3. Proximity-Based Discovery
- Haversine distance calculation
- Sorts by nearest first
- Works with mock coordinates (no GPS permission needed)

### 4. Trust System
- Simple thumbs-up rating
- Cumulative count
- Optimistic UI (animates immediately)

### 5. Safe Calling
- ACTION_DIAL (not ACTION_CALL)
- Shows dialer, user confirms
- No accidental calls

---

## Troubleshooting Demo Issues

### "No workers showing"
→ Seed data not loaded. Uncomment `seedMockData()` and run once.

### "Firebase permission denied"
→ Database rules not set. Check Rules tab in Firebase Console.

### "App crashes on launch"
→ `google-services.json` not configured. Replace with real file from Firebase.

### "Language still in Kannada"
→ Some screens not fully updated. Known issue, fix in progress.

---

## Demo Environment Setup

### Recommended Setup:
- **Device**: Physical Android phone (better than emulator)
- **Android Version**: 7.0+ (API 24+)
- **Internet**: Required for Firebase sync
- **Screen Mirroring**: Use scrcpy or Vysor for projection

### Quick Commands:
```bash
# Build and install
./gradlew installDebug

# Launch app
adb shell am start -n com.manekelsa.app/.MainActivity

# Clear app data (reset language selection)
adb shell pm clear com.manekelsa.app

# Check logs
adb logcat | grep ManeKelsa
```

---

## Post-Demo Q&A Prep

**Q: How does it work offline?**
A: Firebase caches data locally. Workers can toggle availability offline; it syncs when connection returns.

**Q: How do you prevent fake profiles?**
A: Phase 2 will add phone OTP verification and Aadhaar integration.

**Q: What about payment?**
A: Currently cash-based. Future: UPI integration for advance booking.

**Q: Scalability?**
A: Firebase Realtime Database handles 100k concurrent connections. For larger scale, we'd shard by city.

**Q: Why not use GPS?**
A: Privacy + battery. Workers enter their area manually. Residents see "nearby" based on area names, not exact location.

---

## Next Steps After Demo

1. **Add Hindi translations** (`values-hi/strings.xml`)
2. **Fix remaining hardcoded Kannada** in Profile/About screens
3. **Add phone OTP** for worker verification
4. **Photo upload** to Firebase Storage
5. **Push notifications** when worker becomes available
6. **Admin dashboard** for moderation
7. **Analytics** (Firebase Analytics)

---

## Demo Checklist

- [ ] Firebase project created
- [ ] `google-services.json` replaced
- [ ] Database rules published
- [ ] Mock data seeded
- [ ] App builds successfully
- [ ] Language selection works
- [ ] Worker list appears
- [ ] Filters work
- [ ] Call button opens dialer
- [ ] Thumbs-up animates
- [ ] Availability toggle syncs
- [ ] Firebase Console open for live demo
