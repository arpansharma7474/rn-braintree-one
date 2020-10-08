import { NativeModules } from 'react-native';

type RnBraintreeType = {
  multiply(a: number, b: number): Promise<number>;
};

const { RnBraintree } = NativeModules;

export default RnBraintree as RnBraintreeType;
