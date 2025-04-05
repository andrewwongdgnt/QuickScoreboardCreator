package com.dgnt.quickScoreboardCreator.feature.history.domain.usecase

import javax.inject.Inject

class ValidateHistoryDetailsUseCase @Inject constructor() {
    operator fun invoke(title: String) = title.isNotBlank()
}