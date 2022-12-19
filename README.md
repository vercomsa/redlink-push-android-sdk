# Getting started

## Gradle setup

Add required dependencies to your gradle config file `app/build.gradle`.

```gradle
implementation 'pl.redlink:push:1.12.1'
implementation 'androidx.appcompat:appcompat:1.4.2'
implementation 'com.google.firebase:firebase-messaging:23.1.0'
```

Add required repository

```gradle
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://redlinkv1.jfrog.io/artifactory/default-maven-local'
        }
    }
}
```

NOTE: Redlink Push uses AndroidX dependencies (aka Android Support Library). We recommend to update project to AndroidX to avoid conflicts (https://developer.android.com/jetpack/androidx/migrate).

## Firebase platform integration
Redlink push is based on firebase platform. To configure it on Android check the documentation:
https://firebase.google.com/docs/android/setup
## Redlink SDK integration
### Redlink initialization

Add required tokens to `string.xml` resources, obtained from the Redlink dashboard.

```xml
<string name="redlink_app_id"></string>
<string name="redlink_token"></string>
<string name="redlink_secret"></string>
<string name="redlink_events_token"></string>
```

Add required firebase sender id, obtained from the Firebase dashboard (`Settings -> Cloud Messaging -> Sender ID`)
```xml
<string name="fcm_sender_id"></string>
```

For a proper actions handling extends your base activity using `RedlinkActivity` or pass `onNewIntent` call to the LifecycleController:

```kotlin
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    RedlinkApp.lifecycleController.processOnNewIntent(this, intent)
}
```

SDK initializes automatically, there are no other actions required.

# Push Notifications

## Push message

To handle redlink push message in the app, register broadcast receiver:

```kotlin
val intentFilter = IntentFilter(RedlinkFirebaseMessagingService.PUSH_ACTION)
registerReceiver(pushBroadcast, intentFilter)

private val pushBroadcast: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      val pushMessage = intent?.getParcelableExtra<PushMessage>(RedlinkFirebaseMessagingService.EXTRA_PUSH_MESSAGE)
      //todo do something with the push message
    }
}
```

Remember to unregister broadcast when it is no longer needed.

## Actions
Actions can be invoked by interacting on notification (tap action) or notification action buttons.
Each action has one of the four action types:
- BROWSER - opens the default browser
- WEBVIEW - opens the internal browser using Chrome Tabs or WebView
- DEEPLINK - opens the app using `ACTION_VIEW` action with the deeplink url
- NONE - opens the app

To configure ChromeTabs, pass the builder via `RedlinkApp.customTabsBuilder()` method.

```kotlin
val tabsBuilder = CustomTabsIntent.Builder()
RedlinkApp.customTabsBuilder(tabsBuilder)
```

To handle actions manually, register handler via `RedlinkApp.customActionHandler` method.

```kotlin
val handler = object : RedlinkActionHandler {
   override handleAction(applicationContext: Context, action: Action) {
     //todo handle action
   }
}
RedlinkApp.customActionHandler(handler)
```
## In-App Pushes
Represents the last push, which there was no action. In-App pushes are presented in the form of a native dialog based on title, body and actions.

To handle In-App push manually, register handler via `RedlinkApp.customInAppPushHandler` method.

```kotlin
val handler = object : InAppPushHandler {
   override fun handleLastPush(activity: Activity, pushMessage: PushMessage) {
     //todo handle last push
   }
}
RedlinkApp.customInAppPushHandler(handler)
```

## Channels
At this moment all Redlink pushes has one channel with id equals to `pl.redlink.push.default_channel_id`.

## Custom push handling
To handle manually pushes create own service that extends `RedlinkFirebaseMessagingService`, than override `onPushMessageReceived` method. There is still an option to show notification by Redlink SDK by calling `super.onPushMessageReceived(pushMessage)` or `RedlinkNotification.handlePushMessage(applicationContext, pushMessage)`.
```kotlin
class MyService : RedlinkFirebaseMessagingService() {
    override fun onPushMessageReceived(pushMessage: PushMessage) {
        if (pushMessage.has("key")) {
            //todo handle push
        } else {
            super.onPushMessageReceived(pushMessage)
        }
    }
}
```
Declare service in the manifest with required action in the intent filter.
```xml
<service android:name=".MyService">
  <intent-filter>
      <action android:name="com.google.firebase.MESSAGING_EVENT" />
  </intent-filter>
</service>
```

# User identification
User identification feature is provided by the RedlinkUser object.
## Update information about the user
To update information about the user developer should use a RedlinkUser.Edit class instance.
Developer is currently able to update following data: email, phone number, first name, last name and the company.
Additionally he can provide some extra data in form of key-value pairs.
Example:
```kotlin
  RedLinkUser.Edit()
        .email("user@redlink.pl")
        .phone("+48123456789")
        .customValue("age", 25) 
        .customValue("premium", false)
        .save()
```
Please note that developer must call a `save()` function to complete the update process.

Notes
- Custom values support `Int`, `String`, `Boolean`, `Date` types
- `email` method requires valid email format
- `email`, `companyName`, `firstName`, `lastName` can be up to 64 length characters

## Remove information about the user
To remove all information about the user, developer should simply invoke a `remove()` function on the RedlinkUser object.

```kotlin
RedlinkUser.remove()
```

If you want also to unsubscribe user from Redlink Push Notification Services you can also use additional parameter while removing user like so:
```kotlin
RedlinkUser.remove(deletePushToken = true)
```

You can use that when the user did sign out and you don't want to send notifications for that user.
To make the current user's device to receive push notifications back again you need to call:
```kotlin
RedlinkUser.Edit().save()
```

To remove only extra data assigned to user developer should simply invoke a `removeCustomValues()` function on a `RedlinkUser.Edit` class instance.

```kotlin
  RedlinkUser.Edit()
        .removeCustomValues()
        .save()
```

Please note that developer must call a `save()` function to complete the removal process.

# Deeplinking
Deeplink can be invoked via tap notification and notification action buttons, that just runs Intent with the `ACTION_VIEW` action that contains specific URL. Handle it the same as in the official documentation:

https://developer.android.com/training/app-links/deep-linking

# Analytics
Custom analytics events feature is provided by the RedlinkAnalytics object.
To send custom analytics events developer should call a `trackEvent(String)` function on the RedlinkAnalytics object.

```kotlin
  RedlinkAnalytics.trackEvent("PRODUCT_DETAILS_CLICKED")
```

To provide some extra data as parameters developer should call a `trackEvent(String, Map<String, Any>)` function on the RedlinkAnalytics object.

Notes
- Param values support `Int`, `String`, `Boolean`, `Date` types.
- Event name can be up to 64 length characters
- Param key can be up to 64 length characters

Java:
```java
  HashMap<String, Object> params = new HashMap<>();
  params.put("ITEM_ID", item.getId());
  params.put("APP_SCREEN", getScreenName());
  RedlinkAnalytics.trackEvent("PRODUCT_CLICKED", params);
```
Kotlin:
```kotlin
  val params = mapOf(
          "ITEM_ID" to item.id,
          "APP_SCREEN" to screenName)
  RedlinkAnalytics.trackEvent("PRODUCT_CLICKED", params)
```