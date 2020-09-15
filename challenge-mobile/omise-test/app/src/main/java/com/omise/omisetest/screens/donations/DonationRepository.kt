package com.omise.omisetest.screens.donations

import co.omise.android.api.Client
import co.omise.android.api.RequestListener
import co.omise.android.models.CardParam
import co.omise.android.models.Token
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charge
import com.omise.omisetest.common.models.CreditCard
import com.omise.omisetest.common.network.DonationsApiService
import timber.log.Timber

class DonationRepository(private val donationsApiService: DonationsApiService, private val omiseClient: Client) {
    suspend fun charge(charge: Charge): String {
        return donationsApiService.createDonation(charge.toNetworkLevel())
    }

    fun createToken(creditCard: CreditCard, tokenCallback: (String?) -> Unit): ApiStatus {
        val cardParam = CardParam(
            name = creditCard.cardholderName,
            number = creditCard.cardNumber,
            expirationMonth = creditCard.expMonth,
            expirationYear = creditCard.expYear,
            securityCode = creditCard.securityCode)
        val request = Token.CreateTokenRequestBuilder(cardParam).build()

        omiseClient.send(request, object : RequestListener<Token>{
            override fun onRequestSucceed(model: Token) {
                tokenCallback(model.id)
            }

            override fun onRequestFailed(throwable: Throwable) {
                tokenCallback(null)
            }
        })

        return ApiStatus.Success
    }
}