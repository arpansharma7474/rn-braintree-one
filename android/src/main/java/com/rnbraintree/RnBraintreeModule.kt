package com.rnbraintree

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.braintreepayments.api.BraintreeFragment
import com.braintreepayments.api.Card
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.braintreepayments.api.exceptions.ErrorWithResponse
import com.braintreepayments.api.exceptions.InvalidArgumentException
import com.braintreepayments.api.interfaces.BraintreeCancelListener
import com.braintreepayments.api.interfaces.BraintreeErrorListener
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener
import com.braintreepayments.api.models.CardBuilder
import com.braintreepayments.api.models.CardNonce
import com.braintreepayments.api.models.ThreeDSecureRequest
import com.facebook.react.bridge.*
import com.google.gson.Gson
import java.util.*


class RnBraintreeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener {

  override fun getName(): String {
    return "RnBraintree"
  }

  private val REQUEST_CODE = 65000

  private val USER_CANCELLATION = "USER_CANCELLATION"
  private val AUTHENTICATION_UNSUCCESSFUL = "AUTHENTICATION_UNSUCCESSFUL"
  private var token: String? = null

  private lateinit var mBraintreeFragment: BraintreeFragment

  private lateinit var promise: Promise

  init {
    reactContext.addActivityEventListener(this)
  }

  //  private var threeDSecureOptions: ReadableMap? = null
  @ReactMethod
  fun setup(token: String, promise: Promise) {
    try {
      this.token = token
      mBraintreeFragment = BraintreeFragment.newInstance(currentActivity as AppCompatActivity?, token)

      mBraintreeFragment.addListener(BraintreeCancelListener { nonceErrorCallback(USER_CANCELLATION) })
      mBraintreeFragment.addListener(PaymentMethodNonceCreatedListener { paymentMethodNonce ->
        if (paymentMethodNonce is CardNonce) {
          nonceCallback(paymentMethodNonce.nonce)
        } else {
          nonceErrorCallback(AUTHENTICATION_UNSUCCESSFUL)
        }
      })
      mBraintreeFragment.addListener(BraintreeErrorListener { error ->
        if (error is ErrorWithResponse) {
          val errorWithResponse = error
          val cardErrors = errorWithResponse.errorFor("creditCard")
          if (cardErrors != null) {
            val gson = Gson()
            val errors: MutableMap<String, String?> = HashMap()
            val numberError = cardErrors.errorFor("number")
            val cvvError = cardErrors.errorFor("cvv")
            val expirationDateError = cardErrors.errorFor("expirationDate")
            val postalCode = cardErrors.errorFor("postalCode")
            if (numberError != null) {
              errors["card_number"] = numberError.message
            }
            if (cvvError != null) {
              errors["cvv"] = cvvError.message
            }
            if (expirationDateError != null) {
              errors["expiration_date"] = expirationDateError.message
            }

            // TODO add more fields
            if (postalCode != null) {
              errors["postal_code"] = postalCode.message
            }
            nonceErrorCallback(gson.toJson(errors))
          } else {
            nonceErrorCallback(errorWithResponse.errorResponse)
          }
        } else {
          nonceErrorCallback(AUTHENTICATION_UNSUCCESSFUL)
        }
      })
      promise.resolve(token)
    } catch (e: InvalidArgumentException) {
      promise.reject(e)
    }
  }

  @ReactMethod
  fun getCardNonce(parameters: ReadableMap, promise: Promise) {
    this.promise = promise
    val cardBuilder = CardBuilder()
      .validate(true)
    if (parameters.hasKey("number")) cardBuilder.cardNumber(parameters.getString("number"))
    if (parameters.hasKey("cvv")) cardBuilder.cvv(parameters.getString("cvv"))

    // In order to keep compatibility with iOS implementation, do not accept expirationMonth and exporationYear,
    // accept rather expirationDate (which is combination of expirationMonth/expirationYear)
    if (parameters.hasKey("expirationDate")) cardBuilder.expirationDate(parameters.getString("expirationDate"))
    if (parameters.hasKey("cardholderName")) cardBuilder.cardholderName(parameters.getString("cardholderName"))
    if (parameters.hasKey("firstName")) cardBuilder.firstName(parameters.getString("firstName"))
    if (parameters.hasKey("lastName")) cardBuilder.lastName(parameters.getString("lastName"))
    if (parameters.hasKey("company")) cardBuilder.company(parameters.getString("company"))
    if (parameters.hasKey("locality")) cardBuilder.locality(parameters.getString("locality"))
    if (parameters.hasKey("postalCode")) cardBuilder.postalCode(parameters.getString("postalCode"))
    if (parameters.hasKey("region")) cardBuilder.region(parameters.getString("region"))
    if (parameters.hasKey("streetAddress")) cardBuilder.streetAddress(parameters.getString("streetAddress"))
    if (parameters.hasKey("extendedAddress")) cardBuilder.extendedAddress(parameters.getString("extendedAddress"))
    Card.tokenize(mBraintreeFragment, cardBuilder)
  }

//  @ReactMethod
//  fun getCardNonceWithThreeDSecure(parameters: ReadableMap, orderTotal: Float, options: ReadableMap, successCallback: Callback?, errorCallback: Callback?) {
//    this.successCallback = successCallback
//    this.errorCallback = errorCallback
//    threeDSecureOptions = options.getMap("threeDSecure")
//    val cardBuilder = CardBuilder()
//      .validate(true)
//    if (parameters.hasKey("number")) cardBuilder.cardNumber(parameters.getString("number"))
//    if (parameters.hasKey("cvv")) cardBuilder.cvv(parameters.getString("cvv"))
//
//    // In order to keep compatibility with iOS implementation, do not accept expirationMonth and exporationYear,
//    // accept rather expirationDate (which is combination of expirationMonth/expirationYear)
//    if (parameters.hasKey("expirationDate")) cardBuilder.expirationDate(parameters.getString("expirationDate"))
//    if (parameters.hasKey("cardholderName")) cardBuilder.cardholderName(parameters.getString("cardholderName"))
//    if (parameters.hasKey("firstName")) cardBuilder.firstName(parameters.getString("firstName"))
//    if (parameters.hasKey("lastName")) cardBuilder.lastName(parameters.getString("lastName"))
//    if (parameters.hasKey("company")) cardBuilder.company(parameters.getString("company"))
//    if (parameters.hasKey("locality")) cardBuilder.locality(parameters.getString("locality"))
//    if (parameters.hasKey("postalCode")) cardBuilder.postalCode(parameters.getString("postalCode"))
//    if (parameters.hasKey("region")) cardBuilder.region(parameters.getString("region"))
//    if (parameters.hasKey("streetAddress")) cardBuilder.streetAddress(parameters.getString("streetAddress"))
//    if (parameters.hasKey("extendedAddress")) cardBuilder.extendedAddress(parameters.getString("extendedAddress"))
//    val threeDSecureRequest = ThreeDSecureRequest()
//    threeDSecureRequest.challengeRequested(true)
//    threeDSecureRequest.amount(orderTotal.toString())
//    threeDSecureRequest.versionRequested(ThreeDSecureRequest.VERSION_2)
//    ThreeDSecure.performVerification(mBraintreeFragment, cardBuilder, threeDSecureRequest)
//  }
//
//  @ReactMethod
//  fun getNonceWithThreeDSecure(parameters: ReadableMap, successCallback: Callback?, errorCallback: Callback?) {
//    this.successCallback = successCallback
//    this.errorCallback = errorCallback
//    if (!parameters.hasKey("nonce")) {
//      this.errorCallback.invoke("Parameter `nonce` is required")
//    } else if (!parameters.hasKey("amount")) {
//      this.errorCallback.invoke("Parameter `amount` is required")
//    } else {
//      val threeDSecureRequest = ThreeDSecureRequest()
//        .nonce(parameters.getString("nonce"))
//        .amount(parameters.getString("amount"))
//        .versionRequested(ThreeDSecureRequest.VERSION_2)
//      ThreeDSecure.performVerification(mBraintreeFragment, threeDSecureRequest) { request, lookup -> ThreeDSecure.continuePerformVerification(mBraintreeFragment, request, lookup) }
//    }
//  }

  fun nonceCallback(nonce: String?) {
    promise.resolve(nonce)
  }

  fun nonceErrorCallback(error: String) {
    promise.reject(Throwable(error))
  }

  @ReactMethod
  fun paymentRequest(options: ReadableMap, promise: Promise) {
    this.promise = promise
    val threeDSecureOptions = ThreeDSecureRequest()
      .amount(options.getString("amount"))
      .versionRequested(ThreeDSecureRequest.VERSION_2)

    val dropInRequest = DropInRequest()
      .vaultManager(false)
      .threeDSecureRequest(threeDSecureOptions)
      .clientToken(token)
      .requestThreeDSecureVerification(true)
      .disablePayPal()
      .disableGooglePayment()
      .disableVenmo()

    currentActivity?.startActivityForResult(dropInRequest.getIntent(currentActivity), REQUEST_CODE)
  }

//  @ReactMethod
//  fun paypalRequest(successCallback: Callback?, errorCallback: Callback?) {
//    this.successCallback = successCallback
//    this.errorCallback = errorCallback
//    PayPal.authorizeAccount(mBraintreeFragment)
//  }

  override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
    Log.e("error", " " + resultCode)
    if (requestCode == REQUEST_CODE) {
      if (data == null) {
        nonceErrorCallback("USER_CANCELLATION")
        return
      }
      when (resultCode) {
        Activity.RESULT_OK -> {
          val result: DropInResult = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT)
          if (result.paymentMethodNonce != null) {
            nonceCallback(result.paymentMethodNonce?.nonce)
          } else {
            nonceErrorCallback("NONCE_ERROR")
          }
          Log.e("result", result.paymentMethodNonce?.nonce)
        }
        Activity.RESULT_CANCELED -> nonceErrorCallback("USER_CANCELLATION")
      }
    }
  }

  override fun onNewIntent(intent: Intent?) {}

}
