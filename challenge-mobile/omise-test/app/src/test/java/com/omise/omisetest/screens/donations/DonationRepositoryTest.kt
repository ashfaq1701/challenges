package com.omise.omisetest.screens.donations

import co.omise.android.api.Client
import co.omise.android.api.Request
import co.omise.android.api.RequestListener
import co.omise.android.models.CardParam
import co.omise.android.models.Token
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charge
import com.omise.omisetest.common.models.CreditCard
import com.omise.omisetest.common.network.DonationsApiService
import com.omise.omisetest.common.utils.OmiseProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset


@ExperimentalCoroutinesApi
class DonationRepositoryTest {
    private lateinit var donationsApiService: DonationsApiService
    private lateinit var omiseClient: Client
    private lateinit var omiseProvider: OmiseProvider
    private lateinit var donationRepository: DonationRepository

    private val charge = Charge(
        name = "Ashfaq Salehin",
        token = "tok_123_abc",
        amount = 2000
    )

    private val card = CreditCard(
        cardNumber = "4242424242424242",
        cardholderName = "Ashfaq Salehin",
        expMonth = 10,
        expYear = 2025,
        securityCode = "307"
    )

    @Before
    fun setUp() {
        donationsApiService = mock()
        omiseClient = mock()
        omiseProvider = mock()
        donationRepository = DonationRepository(donationsApiService, omiseClient, omiseProvider)
    }

    @Test
    @ExperimentalCoroutinesApi
    fun charge_calledWithCharity_returnsResponse() = runBlockingTest {
        val respStr = "{\"success\": true}"
        `when`(donationsApiService.createDonation(any())).thenReturn(respStr)
        val resp = donationRepository.charge(charge)
        assertThat(resp, `is`(respStr))
    }

    @Test
    fun createToken_calledWithMockData_returnsSuccessfully() {
        val mockFunc = mock<(String?) -> Unit>()
        val tokenRequest: Request<Token> = mock()
        val requestListener: RequestListener<Token> = mock()
        val cardParamCaptor: KArgumentCaptor<CardParam> = argumentCaptor<CardParam>()
        `when`(omiseProvider.createTokenRequestBuilder(cardParamCaptor.capture())).thenReturn(tokenRequest)
        `when`(omiseProvider.getRequestListener(any())).thenReturn(requestListener)
        val ret = donationRepository.createToken(card, mockFunc)
        assertThat(ret, `is`(ApiStatus.Success))

        // Get captured value and compare with passed value
        val cardParam = cardParamCaptor.firstValue
        assertThat(cardParam.number, `is`(card.cardNumber))
        assertThat(cardParam.name, `is`(card.cardholderName))
        assertThat(cardParam.expirationMonth, `is`(card.expMonth))
        assertThat(cardParam.expirationYear, `is`(cardParam.expirationYear))
        assertThat(cardParam.securityCode, `is`(cardParam.securityCode))
    }

    @After
    fun resetMocks() {
        reset(donationsApiService)
        reset(omiseClient)
    }
}