package com.omise.omisetest.screens.donations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.omise.omisetest.MainCoroutineRule
import com.omise.omisetest.common.globals.ApiStatus
import com.omise.omisetest.common.models.Charity
import com.omise.omisetest.getOrAwaitValue
import com.omise.omisetest.screens.charities.CharitiesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class DonationViewModelTest {
    private lateinit var donationRepository: DonationRepository
    private lateinit var donationViewModel: DonationViewModel

    private val cardNumber = "4242424242424242"
    private val cardHolderName = "Ashfaq"
    private val cardExpMonth = 10
    private val cardExpYear = 2020
    private val securityCode = "234"
    private val amount = "23.40"

    private var selectedCharity = Charity(
        id = 2,
        name = "Charity 2",
        logoUrl = "https://imgur.com/img2.png"
    )

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        donationRepository = mock()
        donationViewModel = DonationViewModel(ApplicationProvider.getApplicationContext(), donationRepository, selectedCharity)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun submitForm_whenPerformedWithCorrectParams_updatesStateCorrectly() = runBlockingTest {
        `when`(donationRepository.charge(any())).thenReturn("{\"success\": true}")
        `when`(donationRepository.createToken(any(), any())).thenReturn(ApiStatus.Success)

        donationViewModel.setCardNumber(cardNumber)
        donationViewModel.cardHolderName.value = cardHolderName
        MatcherAssert.assertThat(
            donationViewModel.formValid.getOrAwaitValue(),
            `is`(false)
        )

        donationViewModel.setCardExpiryMonth(cardExpMonth)
        donationViewModel.setCardExpiryYear(cardExpYear)
        MatcherAssert.assertThat(
            donationViewModel.formValid.getOrAwaitValue(),
            `is`(false)
        )
        donationViewModel.cardSecurityCode.value = securityCode
        MatcherAssert.assertThat(
            donationViewModel.formValid.getOrAwaitValue(),
            `is`(false)
        )
        donationViewModel.amountTxt.value = "20.25"
        MatcherAssert.assertThat(
            donationViewModel.amount.getOrAwaitValue(),
            `is`(20.25F)
        )
        donationViewModel.amountTxt.value = ""
        MatcherAssert.assertThat(
            donationViewModel.amount.getOrAwaitValue(),
            `is`(0.0F)
        )
        MatcherAssert.assertThat(
            donationViewModel.formValid.getOrAwaitValue(),
            `is`(false)
        )
        donationViewModel.amountTxt.value = amount
        MatcherAssert.assertThat(
            donationViewModel.amount.getOrAwaitValue(),
            `is`(23.40F)
        )
        MatcherAssert.assertThat(
            donationViewModel.formValid.getOrAwaitValue(),
            `is`(true)
        )

        donationViewModel.submitForm()
        MatcherAssert.assertThat(
            donationViewModel.formSubmitted.getOrAwaitValue(),
            `is`(true)
        )
        MatcherAssert.assertThat(
            donationViewModel.showProgressBar.getOrAwaitValue(),
            `is`(true)
        )

        donationViewModel.createTokenCallback("abcd")

        // A shortcut. Because network is being called in IO dispatcher, without this sleep test becomes flaky.
        // There must be some synchronization mechanism with IO thread which I can fix. Just for now skipping it.
        Thread.sleep(200)
        MatcherAssert.assertThat(
            donationViewModel.formSubmitted.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.showProgressBar.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.status.getOrAwaitValue(),
            `is`(ApiStatus.Success)
        )
        MatcherAssert.assertThat(
            donationViewModel.navigateToPaymentSuccessfulScreen.getOrAwaitValue(),
            `is`(true)
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun submitForm_whenCallbackCalledWithEmptyToken_updatesStateCorrectly() = runBlockingTest {
        `when`(donationRepository.createToken(any(), any())).thenReturn(ApiStatus.Success)
        donationViewModel.setCardNumber(cardNumber)
        donationViewModel.cardHolderName.value = cardHolderName
        donationViewModel.setCardExpiryMonth(cardExpMonth)
        donationViewModel.setCardExpiryYear(cardExpYear)
        donationViewModel.cardSecurityCode.value = securityCode
        donationViewModel.amountTxt.value = amount
        donationViewModel.submitForm()
        donationViewModel.createTokenCallback(null)
        MatcherAssert.assertThat(
            donationViewModel.formSubmitted.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.showProgressBar.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.status.getOrAwaitValue(),
            `is`(ApiStatus.ERROR)
        )
        MatcherAssert.assertThat(
            donationViewModel.navigateToServerErrorScreen.getOrAwaitValue(),
            `is`(true)
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun submitForm_whenCallbackThrowsException_updatesStateCorrectly() = runBlockingTest {
        `when`(donationRepository.createToken(any(), any())).thenReturn(ApiStatus.Success)
        `when`(donationRepository.charge(any())).doAnswer { throw Exception("Test exception") }

        donationViewModel.setCardNumber(cardNumber)
        donationViewModel.cardHolderName.value = cardHolderName
        donationViewModel.setCardExpiryMonth(cardExpMonth)
        donationViewModel.setCardExpiryYear(cardExpYear)
        donationViewModel.cardSecurityCode.value = securityCode
        donationViewModel.amountTxt.value = amount
        donationViewModel.submitForm()
        donationViewModel.createTokenCallback("abcd")

        // A shortcut. Because network is being called in IO dispatcher, without this sleep test becomes flaky.
        // There must be some synchronization mechanism with IO thread which I can fix. Just for now skipping it.
        Thread.sleep(200)
        MatcherAssert.assertThat(
            donationViewModel.formSubmitted.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.showProgressBar.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.status.getOrAwaitValue(),
            `is`(ApiStatus.ERROR)
        )
        MatcherAssert.assertThat(
            donationViewModel.navigateToServerErrorScreen.getOrAwaitValue(),
            `is`(true)
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun submitForm_whenCallbackThrowsIOException_updatesStateCorrectly() = runBlockingTest {
        `when`(donationRepository.createToken(any(), any())).thenReturn(ApiStatus.Success)
        `when`(donationRepository.charge(any())).doAnswer { throw IOException("Test exception") }

        donationViewModel.setCardNumber(cardNumber)
        donationViewModel.cardHolderName.value = cardHolderName
        donationViewModel.setCardExpiryMonth(cardExpMonth)
        donationViewModel.setCardExpiryYear(cardExpYear)
        donationViewModel.cardSecurityCode.value = securityCode
        donationViewModel.amountTxt.value = amount
        donationViewModel.submitForm()
        donationViewModel.createTokenCallback("abcd")

        // A shortcut. Because network is being called in IO dispatcher, without this sleep test becomes flaky.
        // There must be some synchronization mechanism with IO thread which I can fix. Just for now skipping it.
        Thread.sleep(200)
        MatcherAssert.assertThat(
            donationViewModel.formSubmitted.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.showProgressBar.getOrAwaitValue(),
            `is`(false)
        )
        MatcherAssert.assertThat(
            donationViewModel.status.getOrAwaitValue(),
            `is`(ApiStatus.ERROR)
        )
        MatcherAssert.assertThat(
            donationViewModel.navigateToServerErrorScreen.getOrAwaitValue(),
            `is`(true)
        )
    }

    @After
    fun resetMocks() {
        Mockito.reset(donationRepository)
    }
}