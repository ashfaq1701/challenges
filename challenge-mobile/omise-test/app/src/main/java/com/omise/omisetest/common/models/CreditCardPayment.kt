package com.omise.omisetest.common.models

data class CreditCardPayment(
    var cardNumber: String,
    var cardholderName: String,
    var expMonth: Int,
    var expYear: String,
    var securityCode: String,
    var amount: Float
)