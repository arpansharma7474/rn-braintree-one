# rn-braintree-one

This package provides latest iOS(v4) and Android(v3) Braintree SDK support for React Native.

## Installation

```sh
npm install rn-braintree-one --save
```

### Linking
  RN >= 60
    Will be linked automatically.  
      
   RN < 60
    Please refer manual linking docs.

 ### iOS  
 - Open Podfile and add following line below `platform :ios, '9.0'`
    ```
    use_modular_headers!
    ```
 - Update 
 
    `pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec'`  
    `pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec'`
  
   to
  
    `pod 'glog', :podspec => '../node_modules/react-native/third-party-podspecs/glog.podspec', :modular_headers => false`   
    `pod 'Folly', :podspec => '../node_modules/react-native/third-party-podspecs/Folly.podspec', :modular_headers => false`
  
     And remove the following 

     `add_flipper_pods!`  
     `post_install do |installer|`  
     `flipper_post_install(installer)`  
     `end`  

  - Remove the following lines from AppDelegate.m
      `#if DEBUG`  
      `#import <FlipperKit/FlipperClient.h>`  
      `#import <FlipperKitLayoutPlugin/FlipperKitLayoutPlugin.h>`  
      `#import <FlipperKitUserDefaultsPlugin/FKUserDefaultsPlugin.h>`  
      `#import <FlipperKitNetworkPlugin/FlipperKitNetworkPlugin.h>`  
      `#import <SKIOSNetworkPlugin/SKIOSNetworkAdapter.h>`  
      `#import <FlipperKitReactPlugin/FlipperKitReactPlugin.h>`  
      `static void InitializeFlipper(UIApplication *application) {`  
      `FlipperClient *client = [FlipperClient sharedClient];`  
      `SKDescriptorMapper *layoutDescriptorMapper = [[SKDescriptorMapper alloc] initWithDefaults];`  
      `[client addPlugin:[[FlipperKitLayoutPlugin alloc] initWithRootNode:application withDescriptorMapper:layoutDescriptorMapper]];`  
      `[client addPlugin:[[FKUserDefaultsPlugin alloc] initWithSuiteName:nil]];`  
      `[client addPlugin:[FlipperKitReactPlugin new]];`  
      `[client addPlugin:[[FlipperKitNetworkPlugin alloc] initWithNetworkAdapter:[SKIOSNetworkAdapter new]]];`  
      `[client start];`  
      `}`  
      `#endif`  
    and  
      `#if DEBUG`  
      `InitializeFlipper(application);`  
      `#endif`  

  - This package uses Swift for which you need to add Swift file to your Xcode project.
    - Open your project in Xcode.
    - File → New → File… (or CMD+N)
    - Select Swift File
    - Name your file 'Swift' File.
    - In the Group dropdown, make sure to select the group 'Your App Name', not the project itself. 
    - After you create the Swift file, you should be prompted to choose if you want to configure an Objective-C Bridging Header. Select “Create Bridging Header”.
    - This file is usually named YourProject-Bridging-Header.h. Don’t change this name manually, because Xcode configures the project with this exact filename.
    - Clean and Rebuild Project

 ### Android  
  
  - Update the minSdkVersion to atleast 21 in the `app/build.gradle` file  
  - In project `build.gradle`  
      Update  

         dependencies {
             classpath("com.android.tools.build:gradle:3.5.4")  
             ...  
         }  

      And  
      
         allprojects {  
         repositories {  
            mavenLocal()  
            maven {
               url "https://cardinalcommerceprod.jfrog.io/artifactory/android"
                credentials {
                  username 'braintree_team_sdk'
                  password 'AKCp8jQcoDy2hxSWhDAUQKXLDPDx6NYRkqrgFLRc3qDrayg6rrCbJpsKKyMwaykVL8FWusJpp'
                }  
            }
           ...  
        }

### Post Install
   #### Run  
        npm install  
        cd ios  
        pod install  
    
   If not successful, refer [Braintree docs](https://developers.braintreepayments.com/guides/drop-in/setup-and-integration/android/v3)

## Usage

```js
import RnBraintree from "rn-braintree-one";

// Initialize package
  RnBraintree.setup('your client Token')
  .then((res) => {
    console.log('Success in setup', res)
  })
  .catch((err) => {
    console.log('Error in setup', err)
  });

// Show Payment Screen (Android & iOS)

  RnBraintree.paymentRequest({
  bgColor: '#FF0000',
  tintColor: '#0000FF',
  amount: '20'
  })
  .then((res) => {
    console.log('Success in onPaymentPress', res)
  })
  .catch((err) => {
    console.log('Error in onPaymentPress', err)
  });
 
 // Card Tokenization
  RnBraintree.getCardNonce({
  cardholderName: 'Test User',
  number: '4111111111111111',
  expirationDate: '10/2020', // or "10/2020" or any valid date
  cvv: '400',
  shouldValidateCard : true // optional : Braintree checks for card validity if true
  })
  .then((res) => {
    console.log('Success in onCardPress', res)
  })
  .catch((err) => {
    console.log('Error in onCardPress', err)
  });
```

## Why was this package created?
This package was created due to unavailability of a reliable Braintree package for latest Android and iOS SDK's. Google Play Store rejected our Android app for using old Braintree SDK version (2). If you also face this issue (`Your app(s) are using an unsafe implementation of the HostnameVerifier interface`), you can use this package.

Please let us know of your comments and feedback.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
