import * as React from 'react';
import { StyleSheet, Button, SafeAreaView } from 'react-native';
import RnBraintree from 'rn-braintree-one';
import { Log } from './Logger';

export default function App() {
  React.useEffect(() => {
    async function innerAsync() {
      try {
        const token =
          "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiI5YzRiYWQxMmEzNzc1ZmRjNDJjMmJiMzkwMWQ4ODhlODQwZTkzZDYxYzgyOTgyYjU1MDBlZDNhYzUzZWRkMjM5fGNyZWF0ZWRfYXQ9MjAxNi0wNi0xOFQxNDoyMjo1MS43Mzc1OTcwMjcrMDAwMFx1MDAyNm1lcmNoYW50X2lkPWNjOWY2OHZ3Y2NrdDYzdGpcdTAwMjZwdWJsaWNfa2V5PW0ycGJ4a3F4Yzl5ZHQyYzIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvY2M5ZjY4dndjY2t0NjN0ai9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzL2NjOWY2OHZ3Y2NrdDYzdGovY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tL2NjOWY2OHZ3Y2NrdDYzdGoifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiV2h5d2FpdCB0ZXN0IiwiY2xpZW50SWQiOm51bGwsInByaXZhY3lVcmwiOiJodHRwOi8vZXhhbXBsZS5jb20vcHAiLCJ1c2VyQWdyZWVtZW50VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3RvcyIsImJhc2VVcmwiOiJodHRwczovL2Fzc2V0cy5icmFpbnRyZWVnYXRld2F5LmNvbSIsImFzc2V0c1VybCI6Imh0dHBzOi8vY2hlY2tvdXQucGF5cGFsLmNvbSIsImRpcmVjdEJhc2VVcmwiOm51bGwsImFsbG93SHR0cCI6dHJ1ZSwiZW52aXJvbm1lbnROb05ldHdvcmsiOnRydWUsImVudmlyb25tZW50Ijoib2ZmbGluZSIsInVudmV0dGVkTWVyY2hhbnQiOmZhbHNlLCJicmFpbnRyZWVDbGllbnRJZCI6Im1hc3RlcmNsaWVudDMiLCJiaWxsaW5nQWdyZWVtZW50c0VuYWJsZWQiOnRydWUsIm1lcmNoYW50QWNjb3VudElkIjoiV2h5d2FpdF9zdmVuc2tfdGVzdDIiLCJjdXJyZW5jeUlzb0NvZGUiOiJTRUsifSwiY29pbmJhc2VFbmFibGVkIjpmYWxzZSwibWVyY2hhbnRJZCI6ImNjOWY2OHZ3Y2NrdDYzdGoiLCJ2ZW5tbyI6Im9mZiJ9";
        const res = await RnBraintree.setup(token)
        Log('Success in setup', res)
      } catch (err) {
        Log('Error in setup', err)
      }
    }
    innerAsync()
  }, []);

  async function onPaymentPress() {
    try {
      const res = await RnBraintree.paymentRequest({
        bgColor: '#FF0000',
        tintColor: '#0000FF',
        amount: '20'
      })
      Log('Success in onPaymentPress', res)
    } catch (err) {
      Log('Error in onPaymentPress', err)
    }
  }

  async function onCardPress() {
    try {
      const res = await RnBraintree.getCardNonce({
        cardholderName: 'James Bond',
        number: '4111111111111111',
        expirationDate: '10/2020', // or "10/2020" or any valid date
        cvv: '400',
        shouldValidateCard : true // optional : Braintree checks for card validity if true
      })
      Log('Success in onCardPress', res)
    } catch (err) {
      Log('Error in onCardPress', err)
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <Button onPress={onCardPress} title={'Add a card'}
      />

      <Button onPress={onPaymentPress} title={'Start Payment'}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
