package com.omise.omisetest.common.models

import com.omise.omisetest.common.network.Charge

data class Charge (
    val name: String,
    val token: String,
    val amount: Int
) {
    fun toNetworkLevel(): Charge {
        return Charge(
            name, token, amount
        )
    }
}