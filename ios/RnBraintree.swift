import BraintreeDropIn
import Braintree

@objc(RnBraintree)
class RnBraintree: NSObject {
    
    var braintreeClient : BTAPIClient?
    
    @objc(setup:withResolver:withRejecter:)
    func setup(token: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        braintreeClient = BTAPIClient(authorization : token)
        resolve("Braintree Initialized Successfully")
    }
    
    @objc(getCardNonce:withResolver:withRejecter:)
    func getCardNonce(cardOptions: NSDictionary, resolve: @escaping RCTPromiseResolveBlock,reject: @escaping RCTPromiseRejectBlock) -> Void {
        let cardClient = BTCardClient(apiClient: braintreeClient!)
        let date = cardOptions["expirationDate"] as! String
        let monthArray = date.components(separatedBy: "/")
        let month = monthArray.first as! String
        let year = monthArray.last as! String
        
        let numberStr =  cardOptions["number"] as! String
        let cvvStr =  cardOptions["cvv"] as! String
        
        print(month, year, numberStr, cvvStr, "Hello")
        
        let card = BTCard(number:numberStr, expirationMonth: month, expirationYear: year, cvv: cvvStr)
        cardClient.tokenizeCard(card) { (tokenizedCard, error) in
            if((error) != nil){
                reject("400",error?.localizedDescription, error)
            }else{
                resolve(tokenizedCard?.nonce)
            }
                
        }
    }
    
    @objc(paymentRequest:withResolver:withRejecter:)
    func paymentRequest(paymentOptions: NSDictionary, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
//        let request =  BTDropInRequest()
        
        resolve("Hello")
    }
    
}
