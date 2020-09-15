package com.omise.omisetest.screens.donations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.io.Serializable
import kotlin.Triple

class CreditCardComplexLiveData(val cardNumber: LiveData<String>, val cardholderName: LiveData<String>, val expMonth: LiveData<Int>, val expYear: LiveData<Int>, val securityCode: LiveData<String>, val amount: LiveData<Float>):
    MediatorLiveData<Hexa<String, String, Int, Int, String, Float>>() {
    init {
        addSource(cardNumber) {
            value = Hexa(it, cardholderName.value, expMonth.value, expYear.value, securityCode.value, amount.value)
        }
        addSource(cardholderName) {
            value = Hexa(cardNumber.value, it, expMonth.value, expYear.value, securityCode.value, amount.value)
        }
        addSource(expMonth) {
            value = Hexa(cardNumber.value, cardholderName.value, it, expYear.value, securityCode.value, amount.value)
        }
        addSource(expYear) {
            value = Hexa(cardNumber.value, cardholderName.value, expMonth.value, it, securityCode.value, amount.value)
        }
        addSource(securityCode) {
            value = Hexa(cardNumber.value, cardholderName.value, expMonth.value, expYear.value, it, amount.value)
        }
        addSource(amount) {
            value = Hexa(cardNumber.value, cardholderName.value, expMonth.value, expYear.value, securityCode.value, it)
        }
    }
}

class PairBooleanLiveData(val first: LiveData<Boolean>, val second: LiveData<Boolean>):
    MediatorLiveData<Pair<Boolean?, Boolean?>>() {
    init {
        addSource(first) {
            value = Pair(it, second.value)
        }
        addSource(second) {
            value = Pair(first.value, it)
        }
    }
}

data class Hexa<out A, out B, out C, out D, out E, out F>(
    public val first: A?,
    public val second: B?,
    public val third: C?,
    public val fourth: D?,
    public val fifth: E?,
    public val sixth: F?
) : Serializable {

    /**
     * Returns string representation of the [Triple] including its [first], [second] and [third] values.
     */
    public override fun toString(): String = "($first, $second, $third, $fourth, $fifth, $sixth)"
}