# Changelog

All notable changes to this project are documented in this file.
Format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

## [1.16.12] - 2026-09-30

### Fixed
- Crash `IllegalArgumentException: Given work is not active` in the events service, which
  the 1.16.10 fix did not cover. Events are now sent by a WorkManager worker (requires a
  network connection) instead of the `EventsService` `JobIntentService`.
- The same event could be sent more than once when a stopped send overlapped with a new one,
  or when the backend accepted an event but the send was interrupted before it was removed
  locally. Sends are now serialized within the process and an accepted event is always
  removed.
- HTTP requests had no connect or read timeout and could block indefinitely on a stalled
  connection. They now time out after 15 s (connect) and 30 s (read).

### Removed
- `pl.redlink.push.service.EventsService`. It was only used internally by the SDK.

## [1.16.11] - 2026-09-24

### Added
- Published `SECURITY.md` with a coordinated vulnerability disclosure policy.
- Published a CycloneDX Software Bill of Materials (`sbom/bom.json`, `sbom/bom.xml`)
  for the `pl.redlink:push` library, regenerated on every release.

### Security

- **Impact:** a credential for the account that publishes `pl.redlink:push` to JFrog
  Artifactory was hard-coded in the SDK's internal build scripts, in the private SDK source
  repository.
- **Severity:** Medium (supply-chain relevant). There is no indication the credential was
  used by an unauthorized party.
- **Affected:** the private SDK source repository only. No published `pl.redlink:push`
  version and no file in this public repository is affected.
- **Fixed in:** 1.16.11 — publishing credentials are no longer stored in source code.
- **Action required from integrators:** none.

## [1.16.10] - 2026-08-07

### Fixed
- Crash `IllegalArgumentException: Given work is not active` when Android 16 stops a
  background job while an SDK request is still running. Background services now stop
  cleanly and re-enqueue their work on the next app foreground.

## [1.16.9] - 2026-07-06

### Added
- Notification actions that cannot be handled by any installed activity now fall back to
  opening the app's launcher activity instead of being dropped.

## [1.16.8] - 2026-06-25

### Fixed
- `ActivityNotFoundException` when a notification action pointed at an activity that could
  not be resolved on the device.

## [1.16.7] - 2026-03-17

### Fixed
- User data updates made before the FCM token was available are now stored and sent right
  after the device registers its token, instead of being lost.

## [1.16.6] - 2026-01-26

### Changed
- `compileSdk` / `targetSdk` raised to 36, Java 17 bytecode; Android Gradle Plugin 8.7.3,
  Gradle 8.10.2.
- Dependency updates: `firebase-messaging` 24.1.0, `appcompat` 1.7.0, `media` 1.7.0,
  `browser` 1.8.0, `annotation` 1.9.1, `work-runtime-ktx` 2.10.0, Kotlin coroutines 1.9.0.

### Removed
- `jcenter()` repository.

## [1.16.5] - 2025-11-12

### Fixed
- Intent unmarshalling error when the SDK received an intent on Android 11 and below.

## [1.16.4] - 2025-10-09

### Fixed
- Crash when an incoming intent carried a `Parcelable` of a class unknown to the SDK
  (e.g. from third-party deep links). Unknown extras are now skipped.

## [1.16.3] - 2025-09-19

### Changed
- Kotlin toolchain update and related source adjustments.

## [1.16.2] - 2025-05-19

### Fixed
- `RedlinkUser.detachToken()` now clears locally stored user data before detaching the token.

## [1.16.1] - 2025-05-14

### Changed
- User update requests now carry the full user payload and a timestamp so the backend
  applies the latest state regardless of delivery order.

## [1.16.0] - 2025-04-22

### Added
- `RedlinkUser.detachToken()` to detach the current push token from the user without
  unsubscribing the device.

### Changed
- `RedlinkUser.remove(deletePushToken = true)` now also detaches the token.

## [1.15.0] - 2025-03-21

### Added
- Timestamp attached to device, user and status requests.

### Changed
- Minimum supported Android SDK raised from 19 to 21.

## [1.14.1] - 2024-06-28

Dependency update.

## [1.14.0] - 2024-06-21

### Added
- `RedlinkUser.Edit().externalId(...)`.

### Changed
- Push intent handling now uses `PushMessage.fromIntent(intent)`.
- Distribution is now exclusively via the JFrog Artifactory endpoint introduced in 1.5.4.

## [1.12.3] - 2023-01-04

Dependency update.

## [1.12.1] - 2022-12-19

Dependency update.

## [1.12.0] - 2022-12-12

Dependency update.

## [1.11.0] - 2022-11-24

Dependency update.

## [1.7.0] - 2022-03-18

### Added
- `RedlinkUser.remove(deletePushToken = true)` to also unsubscribe the device from push notifications on user removal.

## [1.5.4] - 2022-01-05

### Changed
- Migrated distribution to JFrog Artifactory.

## [1.3.5] - 2021-05-14

Maintenance release.

## [1.3.0] - 2020-03-18

Dependency update.

## [1.2.0] - 2019-12-04

Dependency update.

## [1.0.1] - 2019-06-13

### Added
- Documented field validation constraints (email format, 64-character limits on user and event data fields).

## [1.0.0] - 2019-02-15

Initial release.
