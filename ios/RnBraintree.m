#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RnBraintree, NSObject)
//
//RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
//                 withResolver:(RCTPromiseResolveBlock)resolve
//                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(setup:(NSString *)token
                 withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(getCardNonce:(NSDictionary *)cardOptions
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

RCT_EXTERN_METHOD(paymentRequest:(NSDictionary *)paymentOptions
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)

@end
