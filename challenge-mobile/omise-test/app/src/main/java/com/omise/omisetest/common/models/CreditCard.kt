package com.omise.omisetest.common.models

data class CreditCard(
    val cardNumber: String,
    val cardholderName: String,
    val expMonth: Int,
    val expYear: Int,
    val securityCode: String
)