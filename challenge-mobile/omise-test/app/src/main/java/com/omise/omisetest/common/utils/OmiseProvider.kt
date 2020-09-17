package com.omise.omisetest.common.utils

import co.omise.android.api.Request
import co.omise.android.api.RequestListener
import co.omise.android.models.CardParam
import co.omise.android.models.Token

class OmiseProvider {
    fun createTokenRequestBuilder(cardParam: CardParam): Request<Token> {
        return Token.CreateTokenRequestBuilder(cardParam).build()
    }

    fun getRequestListener(tokenCallback: (String?) -> Unit): RequestListener<Token> {
        return object : RequestListener<Token> {
            override fun onRequestSucceed(model: Token) {
                tokenCallback(model.id)
            }

            override fun onRequestFailed(throwable: Throwable) {
                tokenCallback(null)
            }
        }
    }
}