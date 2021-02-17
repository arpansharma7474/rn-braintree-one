# rn-braintree

This package provides latest iOS(v4) and Android(v3) Braintree SDK support for React Native.

## Installation

```sh
npm install rn-braintree --save
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

  - This package uses Swift for which you need to add Swift file to your project

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
              url  "https://cardinalcommerce.bintray.com/android"  
              credentials {  
              username 'braintree-team-sdk@cardinalcommerce'  
              password '220cc9476025679c4e5c843666c27d97cfb0f951'  
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
import RnBraintree from "rn-braintree";

// ...

const result = await RnBraintree.multiply(3, 7);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
