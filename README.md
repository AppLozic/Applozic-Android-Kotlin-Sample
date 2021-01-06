Applozic Android Kotlin Sample project

### Overview

<img align="right" src="https://raw.githubusercontent.com/AppLozic/Applozic-Android-SDK/master/img/android.png" />


Open source Android Chat SDK / Messaging SDK that lets you add real-time chat and in-app messaging in your mobile (Android, iOS) applications and website.

Signup at [https://www.applozic.com/signup.html](https://www.applozic.com/signup.html?utm_source=github&utm_medium=readme&utm_campaign=android) to get the App ID.


### Getting Started


To integrate the android chat library into your android app, signup at [Applozic](https://www.applozic.com/signup.html?utm_source=github&utm_medium=readme&utm_campaign=android) to get the App ID.

Documentation: [Applozic Android Chat & Messaging SDK Documentation](https://www.applozic.com/docs/android-chat-sdk.html?utm_source=github&utm_medium=readme&utm_campaign=android)


## Prerequisites

* Install or update Android Studio to its latest version.

* Make sure that your project meets these requirements:

  * Targets API level 16 (Jelly Bean) or later

  * Uses Gradle 4.1 or later

## Sample Project

To run the sample project

1. Clone the Applozic Kotlin sample project in the terminal by using the below command
   ``` shell
   git clone https://github.com/AppLozic/Applozic-Android-Kotlin-Sample.git
   ```
2. Open Android studio on your system and select the `open an existing project` option.

3. Select the downloaded folder `Applozic-Android-Kotlin-Sample` to open the project.

4. Set up a physical device or use an emulator to run the sample app.


## Integration
This section will guide you on the integration of Applozic Android SDK in your Kotlin project

### Add the following in your app level build.gradle dependency:

```gradle
dependencies {
   implementation 'com.applozic.communication.uiwidget:mobicomkitui:5.96'
}

```

Add the following in your app build.gradle file inside the android target:

```gradle

android {

   packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/ECLIPSE_.SF'
        exclude 'META-INF/ECLIPSE_.RSA'
        exclude 'META-INF/ECLIPSE_.DSA'
        exclude 'META-INF/*.RSA'
        exclude 'META-INF/*.SF'
        exclude 'META-INF/*.DSA'
    }
}
```

#### Adding in your app AndroidManifest.xml file

Add Meta data, Activity to your app Androidmanifest.xml file:

**Note**: Add meta-data, Activity within application Tag ``` <application> </application> ```

```xml

<meta-data android:name="com.applozic.application.key"
           android:value="YOUR_APPLOZIC_APP_ID" /> <!-- Applozic App ID -->

<meta-data android:name="com.applozic.mobicomkit.notification.smallIcon"
           android:resource="YOUR_LAUNCHER_SMALL_ICON" /> <!-- Launcher white Icon -->

<meta-data android:name="com.google.android.geo.API_KEY"
           android:value="YOUR_GEO_API_KEY" />  <!--Replace with your geo api key from google developer console  -->
<!-- For testing purpose use AIzaSyAYB1vPc4cpn_FJv68eS_ZGe1UasBNwxLI
To disable the location sharing via map add this line ApplozicSetting.getInstance(context).disableLocationSharingViaMap(); in onSuccess of Applozic UserLoginTask -->

<meta-data android:name="com.package.name"
           android:value="${applicationId}" /> <!-- NOTE: Do NOT change this, it should remain same i.e 'com.package.name' -->

<activity android:name="com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity"
          android:configChanges="keyboardHidden|screenSize|smallestScreenSize|screenLayout|orientation"
          android:label="@string/app_name"
          android:parentActivityName="<APP_PARENT_ACTIVITY>"
          android:theme="@style/ApplozicTheme"
          android:launchMode="singleTask"
          tools:node="replace">
     <!-- Parent activity meta-data to support API level 7+ -->
<meta-data android:name="android.support.PARENT_ACTIVITY"
           android:value="<APP_PARENT_ACTIVITY>" />
 </activity>
```
Replace `YOUR_APPLOZIC_APP_ID` with applozic app Id and Replace `APP_PARENT_ACTIVITY` with your app's parent activity.

### Register/Login user account:


```kotlin
  val user = User()
  user.userId = <USER_ID> // userId it can be any unique user identifier NOTE : +,*,? are not allowed chars in userId.
  user.displayName = <DISPLAY_NAME> // displayName is the name of the user which will be shown in chat message
  user.authenticationTypeId = User.AuthenticationType.APPLOZIC.getValue() //User.AuthenticationType.APPLOZIC.getValue() for password verification from Applozic server and User.AuthenticationType.CLIENT.getValue() for access Token verification from your server set access token as password
  user.password = "" // Optional, Pass the password
  user.imageLink = <IMAGE_URL_LINK> // Optional, If you have public image URL of user you can pass here

  Applozic.connectUser(context, user, object : AlLoginHandler {
      override fun onSuccess(registrationResponse: RegistrationResponse?, context: Context?) {
          // After successful registration with Applozic server the callback will come here
      }

      override fun onFailure(
          registrationResponse: RegistrationResponse?,
          exception: Exception?
       ) {
          // If any failure in registration the callback  will come here
           if (registrationResponse != null) {
               Log.i("ApplozicLogin", "Login as been failed:" + registrationResponse.message)
           } else if (exception != null) {
               Log.i("ApplozicLogin", "Login as been failed due to exception:" + exception.localizedMessage)
           }
       }
  })
```

If it is a new user, a new user account will get created else the existing user will be logged in to the application.

#### To check if the user is already login to the app

You can check if user is logged in to applozic in your app or not by using below code:

```kotlin
if (Applozic.isConnected(context)) {
    Log.i("ApplozicUser", "User is logged in")
} else {
    Log.i("ApplozicUser", "User is not logged in")
}
```

### Push Notification Setup

Setup the push notification for notifying the user about a new chat message

#### Adding the Firebase Android configuration file (google-services.json) and Adding the configuration for the google-services plugin

1) If you haven't already added Firebase to your project, add [Firebase to your Android project](https://firebase.google.com/docs/android/setup)

2) Once you're done with the above setup, download the `google-services.json` file from the firebase console,
 Click Settings icon -> Project settings -> Select General tab -> Scroll to the bottom and Select your app -> Click Download google-services.json to obtain your Firebase Android config file.
 Highlighted red in the following image for going to project settings
 
   ![Project-settings](https://raw.githubusercontent.com/AppLozic/Applozic-Android-Kotlin-Sample/main/images/push-notification/project-settings.png "Project-settings")
 

3) Add the Firebase Android configuration(google-services.json) file in the (app-level) directory of your app.

4) To enable Firebase messaging in your app, add the google-services plugin to your Gradle files.

   a) In your root-level (project-level) Gradle file (build.gradle)

     ```gradle
      dependencies {

         // Add the following line:
         classpath 'com.google.gms:google-services:4.3.4'  // Google Services plugin
       }
     ```
   b) In your module (app-level) Gradle file (app/build.gradle), apply the Google Services Gradle plugin

     ```gradle
       apply plugin: 'com.android.application'
        // Add the following line:
       apply plugin: 'com.google.gms.google-services'  // apply Google Services plugin

       android {
          // ...
       }
      ```
#### Obtain the server key from the firebase console

1) From the Firebase console, click Settings icon -> Project settings

2) Once you clicked project settings select `Cloud Messaging tab` under Settings.
   Under Project Credentials, copy your Server Key which is highlighted blue in the following image.
   
   ![Fcm-server-key](https://raw.githubusercontent.com/AppLozic/Applozic-Android-Kotlin-Sample/main/images/push-notification/fcm-server-key.png "Fcm-server-key")
   

**Note:** Make sure you have copied the Server key and not the Legacy key or Sender ID.

#### Update server key in Applozic console

Go to [Applozic console](https://console.applozic.com/settings/pushnotification) under Push Notifications -> GCM/FCM key paste your server key and click Save


#### Setup registerForPushNotification in Applozic.connect task "onSuccess" by pasting below code (refer login [code](https://github.com/AppLozic/Applozic-Android-Kotlin-Sample#registerlogin-user-account)).

```kotlin
FirebaseInstanceId.getInstance().instanceId
   .addOnCompleteListener(OnCompleteListener { task ->
       if (!task.isSuccessful) {
            Log.w("FCM", "getInstanceId failed:", task.exception)
            return@OnCompleteListener
       }
       val token = task.result?.token
       Log.i("FCM", "Found token :$token")
       if (token == null) {
           Log.i("FCM", "FCM token is null returning")
           return@OnCompleteListener
       }
       Applozic.registerForPushNotification(
           context,
           token,
           object : AlPushNotificationHandler {
              override fun onSuccess(registrationResponse: RegistrationResponse?) {
                  Log.i("FCM", "Token updated to applozic server")
              }
              override fun onFailure(
                            registrationResponse: RegistrationResponse?,
                            exception: java.lang.Exception?
                        ) {
              }
       })
   })
```
#### Already have Firebase messaging service code? then go with this (a and b) step else skip this

Add the message handling and token update methods

a) In your app FirebaseMessagingService class in `onMessageReceived` method add the below code

```kotlin
override fun onMessageReceived(remoteMessage: RemoteMessage) {
   Log.i(TAG, "Message data:" + remoteMessage.data)
       if (remoteMessage.data.isNotEmpty()) {
           // Applozic message handling you can copy paste this in your file
           if (Applozic.isApplozicNotification(this, remoteMessage.data)) {
               Log.i(TAG, "Applozic notification processed")
               return
           }
       }
   // Your own app notifications handling
}
```
b) In the same FirebaseMessagingService class in `onNewToken` method add the below code for updating token to applozic.

```kotlin
override fun onNewToken(registrationId: String) {
   super.onNewToken(registrationId)
       // Applozic token update method you can copy paste this in your file
       if (MobiComUserPreference.getInstance(this).isRegistered) {
           try {
               RegisterUserClientService(this).updatePushNotificationId(registrationId)
           } catch (e: Exception) {
                e.printStackTrace()
           }
       }
 }
```
#### In case, if you don't have the existing FirebaseMessagingService related code

Follow this for adding the Firebase Messaging Service class to your project.

a) Download the push notification related file from the Applozic sample project and add FcmListenerService.kt to your project from the below GitHub link [FcmListenerService.kt](https://github.com/AppLozic/Applozic-Android-Kotlin-Sample/blob/main/app/src/main/java/com/applozic/sampleKT/pushnotification/FcmListenerService.kt)

b) Add below code in your app AndroidManifest.xml file set the correct CLASS_PACKAGE name where file exist in your app

```xml
<service android:name="<CLASS_PACKAGE>.FcmListenerService"
android:stopWithTask="false">
    <intent-filter>
         <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
  ```
**Note:** After downloading FcmListenerService.kt make sure to correct the package name inside the FcmListenerService.kt file and match it with your app package where the file exists.

### Launch chat screen
Use the below code to launch the chat screen

```kotlin
val conversationsIntent = Intent(context, ConversationActivity::class.java)
context.startActivity(conversationsIntent)
```


### Launch one to one chat
To launch the one to one conversation pass the RECEIVER_USER_ID and DISPLAY_NAME of the user that you want to chat with.

```kotlin
val individualConversationIntent = Intent(context, ConversationActivity::class.java)
individualConversationIntent.putExtra(ConversationUIService.USER_ID, <RECEIVER_USER_ID>)
individualConversationIntent.putExtra(ConversationUIService.DISPLAY_NAME, <DISPLAY_NAME>) //Pass the display name for the title.
individualConversationIntent.putExtra(ConversationUIService.TAKE_ORDER, true)
startActivity(individualConversationIntent)
```
### Launch group chat
To launch the group chat conversation pass the groupId or clientGroupId

```kotlin
val taskListener: AlGroupInformationAsyncTask.GroupMemberListener = object : AlGroupInformationAsyncTask.GroupMemberListener {
        override fun onSuccess(channel: Channel, context: Context?) {
            val groupConversationIntent = Intent(context, ConversationActivity::class.java)
            groupConversationIntent.putExtra(ConversationUIService.GROUP_ID, channel.key)
            groupConversationIntent.putExtra(ConversationUIService.GROUP_NAME, channel.name)
            groupConversationIntent.putExtra(ConversationUIService.TAKE_ORDER, true)
            context?.startActivity(groupConversationIntent)
        }

        override fun onFailure(channel: Channel?, e: Exception?, context: Context?) {
            // Got some exception
        }
}
AlTask.execute(AlGroupInformationAsyncTask(context, <GroupIDOrClientGroupId>, taskListener))
```

### Logout:
Call the logout on your app logout success to logout the applozic user.
```kotlin
Applozic.logoutUser(context, object : AlLogoutHandler {
    override fun onSuccess(context: Context?) {
       // User has been logout successful
    }

    override fun onFailure(exception: Exception?) {
       // Logout has failed got some exception
    }
 })
 ```


### Documentation:
For advanced options and customization, visit [Applozic Android Chat & Messaging SDK Documentation](https://www.applozic.com/docs/android-chat-sdk.html?utm_source=github&utm_medium=readme&utm_campaign=android)

### Changelog
[Changelog](https://github.com/AppLozic/Applozic-Android-SDK/blob/master/CHANGELOG.md)

#### Features:

 One to one and Group Chat
 
 Image capture
 
 Photo sharing
 
 File attachment
 
 Location sharing
 
 Push notifications
 
 In-App notifications
 
 Online presence
 
 Last seen at 
 
 Unread message count
 
 Typing indicator
 
 Message sent, Read Recipients and Delivery report
 
 Offline messaging
 
 User block / unblock
 
 Multi Device sync
 
 Application to user messaging
 
 Customized chat bubble
 
 UI Customization Toolkit
 
 Cross Platform Support (iOS, Android & Web)

## Help

We provide support over at [StackOverflow] (http://stackoverflow.com/questions/tagged/applozic) when you tag using applozic, ask us anything.

Applozic is the best android chat sdk for instant messaging, still not convinced? Write to us at github@applozic.com and we will be happy to schedule a demo for you.


### Free Android Chat SDK
Special plans for startup and open source contributors, write to us at github@applozic.com 


## Github projects

Android Chat SDK java https://github.com/AppLozic/Applozic-Android-SDK

Web Chat Plugin https://github.com/AppLozic/Applozic-Web-Plugin

iOS Chat SDK https://github.com/AppLozic/Applozic-iOS-SDK

iOS Chat Swift SDK https://github.com/AppLozic/ApplozicSwift
