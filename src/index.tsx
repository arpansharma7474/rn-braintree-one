import { NativeModules } from 'react-native';

type RnBraintreeType = {
  setup(token: string): Promise<string>;
  getCardNonce(object: Object): Promise<string>;
  paymentRequest(object: Object): Promise<string>;
};

const { RnBraintree } = NativeModules;

export default RnBraintree as RnBraintreeType;
