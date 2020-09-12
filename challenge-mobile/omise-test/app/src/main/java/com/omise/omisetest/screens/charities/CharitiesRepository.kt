package com.omise.omisetest.screens.charities

import com.omise.omisetest.common.network.DonationsApiService

class CharitiesRepository(private val donationsApiService: DonationsApiService) {
    suspend fun getCharities(): List<Charity> {
        return donationsApiService.getCharities()
            .map {
                it.toRepositoryLevel()
            }
    }
}