import BraintreeDropIn
import Braintree


@objc(RnBraintree)
class RnBraintree: NSObject {
    
    var braintreeClient : BTAPIClient?
    var clientToken : String?
    
    @objc(setup:withResolver:withRejecter:)
    func setup(token: String, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
        braintreeClient = BTAPIClient(authorization : token)
        clientToken = token
        if(braintreeClient == nil){
            reject("400","Error in Braintree Initialize", nil)
        }
        else{
            resolve("Braintree Initialized Successfully")
        }
    }
    
    @objc(getCardNonce:withResolver:withRejecter:)
    func getCardNonce(cardOptions: NSDictionary, resolve: @escaping RCTPromiseResolveBlock,reject: @escaping RCTPromiseRejectBlock) -> Void {
        let cardClient = BTCardClient(apiClient: braintreeClient!)
        let date = cardOptions["expirationDate"] as! String
        let monthArray = date.components(separatedBy: "/")
        let month = monthArray.first!
        let year = monthArray.last!
        
        let numberStr =  cardOptions["number"] as! String
        let cvvStr =  cardOptions["cvv"] as! String
        
        let card = BTCard(number:numberStr, expirationMonth: month, expirationYear: year, cvv: cvvStr)
        card.shouldValidate = true
        cardClient.tokenizeCard(card) { (tokenizedCard, error) in
            if((error) != nil){
                reject("400",error?.localizedDescription, error)
            }else{
                resolve(tokenizedCard?.nonce)
            }
                
        }
    }
    
    @objc(paymentRequest:withResolver:withRejecter:)
    func paymentRequest(paymentOptions: NSDictionary, resolve: @escaping RCTPromiseResolveBlock,reject: @escaping RCTPromiseRejectBlock) -> Void {
        
        // bgColor
        // tintColor
        // title
        // description
        
//        let bgColor = paymentOptions["bgColor"] as! String?
//        let tintColor = paymentOptions["tintColor"] as! String?
//        if(bgColor != nil){
//            BTUIKAppearance.sharedInstance().tintColor =  returnUIColor(components: bgColor!)
//        }
//        if(tintColor != nil){
//            BTUIKAppearance.sharedInstance().tintColor = returnUIColor(components: tintColor!)
//        }
        let amount = paymentOptions["amount"] as! String
        
        let formatter = NumberFormatter()
        formatter.generatesDecimalNumbers = true
        
        let decimalAmount = formatter.number(from: amount) as? NSDecimalNumber ?? 0
        if(decimalAmount == 0){
            reject("400", "Amount can not be parsed", nil)
        }else {
            let threeDSecureRequest = BTThreeDSecureRequest()
                    threeDSecureRequest.amount = decimalAmount
                    threeDSecureRequest.versionRequested = .version2
            
                    let request = BTDropInRequest()
                    request.applePayDisabled = true
                    request.vaultManager = false
                    request.paypalDisabled = true
                    request.threeDSecureVerification = true
                    request.threeDSecureRequest = threeDSecureRequest
            
                    let dropIn = BTDropInController(authorization: self.clientToken!, request: request)
                    { (controller, result, error) in
                        if (error != nil) {
                            reject("400", error?.localizedDescription, error)
                        } else if (result?.isCancelled == true) {
                            reject("401", "Request Cancelled", nil)
                        } else if let result = result {
                            print("SUCCESS", result)
                            resolve(result.paymentMethod?.nonce)
                        }
                        controller.dismiss(animated: true, completion: nil)
                    }
                    DispatchQueue.main.async {
                        let presentedViewController = RCTPresentedViewController()
                        presentedViewController?.present(dropIn!, animated: true, completion: nil)
                    }
        }
    }
    
}
